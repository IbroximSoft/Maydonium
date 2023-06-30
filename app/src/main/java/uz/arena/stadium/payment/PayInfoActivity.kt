package uz.arena.stadium.payment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.payze.paylib.Payze
import com.payze.paylib.model.CardInfo
import com.santalu.maskedittext.MaskEditText
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uz.arena.stadium.HomeActivity
import uz.arena.stadium.R
import uz.arena.stadium.more.BookingActivity
import uz.arena.stadium.payment.model.request.Data
import uz.arena.stadium.payment.model.request.RequestModel
import uz.arena.stadium.payment.resource.Resource
import uz.arena.stadium.payment.retrofit.ApiClient
import uz.arena.stadium.payment.retrofit.NetworkHelper
import uz.arena.stadium.payment.retrofit.ViewModel
import java.util.*
import kotlin.coroutines.CoroutineContext

class PayInfoActivity : AppCompatActivity(), CoroutineScope {
    var reference: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private lateinit var cardNumber: EditText
    private lateinit var cardExpMonth: MaskEditText
    var Name: String = ""
    var lost: String = ""
    var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_info)

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        cardNumber = findViewById(R.id.paymentInfo_number)
        cardExpMonth = findViewById(R.id.paymentInfo_month)

        val sharedPreference: SharedPreferences =
            getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val cardSaves: String? = sharedPreference.getString("card_number", null)
        val cardExpMonths: String? = sharedPreference.getString("card_month", null)
        cardNumber.setText(cardSaves)
        cardExpMonth.setText(cardExpMonths)

        val currentUserID = auth?.currentUser?.uid
        reference?.child("User")?.child(currentUserID.toString())
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Name = snapshot.child("name").value.toString()
                    lost = snapshot.child("lost").value.toString()
                    phone = snapshot.child("phone").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        val btn: Button = findViewById(R.id.payInfo_btn)
        btn.setOnClickListener {
            transactionID()
        }
    }

    private fun transactionID() {

        reference!!.child("admin_setting")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val summa = snapshot.child("summa").value.toString()
                    payzeCall(summa)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun payzeCall(summa: String) {
        val networkHelper = NetworkHelper(this)
        val viewModel = ViewModel(ApiClient.apiServis, networkHelper)
        val sum = Integer.parseInt(summa)
        val data = RequestModel(data = Data(amount = sum))
        viewModel.getUserData(requestModel = data)
        launch {
            val progressDialog = ProgressDialog(this@PayInfoActivity)
            viewModel.flow.collect {
                when (it) {
                    is Resource.Error -> {
                        progressDialog.dismiss()
                        Toasty.error(
                            this@PayInfoActivity,
                            "Hatolik yuz berdi",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                        Log.d("salomlar", " Hatolik")
                    }
                    Resource.Loading -> {
                        progressDialog.setMessage(getString(R.string.yuklanmoqda))
                        progressDialog.show()
                    }
                    is Resource.Success -> {
                        val transactionID = it.data.response.transactionId
                        Log.d("salomlar", transactionID)
                        payze(transactionID, progressDialog)
                    }
                }
            }
        }
    }

    private fun payze(transactionID: String, progressDialog: ProgressDialog) {

        val time_string: String? = intent.getStringExtra("time_string")
        val time: String? = intent.getStringExtra("time")
        val date: String? = intent.getStringExtra("date")
        val uid_last: String? = intent.getStringExtra("uid_last")
        val filter: String? = intent.getStringExtra("filter")
        val location: String? = intent.getStringExtra("location")
        val name: String? = intent.getStringExtra("name")

        val transactionIds: EditText = findViewById(R.id.transactionIdInput)
        transactionIds.setText(transactionID)
        val payze = Payze(this)
        payze.pay(
            getCardData(),
            transactionID,
            onSuccess = {
                val currentUserID = auth?.currentUser?.uid
                val malumotHas = HashMap<String, String>()
                malumotHas["name"] = Name
                malumotHas["lost"] = lost
                malumotHas["phone"] = phone
                malumotHas["date"] = date.toString()
                malumotHas["time"] = time.toString()
                malumotHas["st_name"] = name.toString()
                malumotHas["location"] = location.toString()
                malumotHas["uid"] = currentUserID.toString()
                val key: String? = reference?.push()?.key
                reference!!.child("user_booking").child(currentUserID.toString())
                    .child(key.toString())
                    .setValue(malumotHas)
                    .addOnCompleteListener {
                    }

                reference!!.child("order_booking").child(uid_last.toString()).child(date.toString())
                    .child(key.toString())
                    .setValue(malumotHas)
                    .addOnCompleteListener {

                    }

                val ref = FirebaseDatabase.getInstance().getReference("order_arena")
                val dummyQuery = ref
                    .child("Football")
                    .child(uid_last.toString())
                    .orderByChild(time_string.toString())
                    .equalTo(time.toString())
                dummyQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dummySnapshot in dataSnapshot.children) {
                            if (dummySnapshot.key == date.toString()) {
                                dummySnapshot.child(time_string.toString()).ref.removeValue()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                reference?.child("order_filter")?.child("Football")?.child(date.toString())
                    ?.child(uid_last.toString())
                    ?.child(filter.toString())?.ref?.removeValue()
                progressDialog.dismiss()
                Toasty.success(
                    this@PayInfoActivity,
                    "To'landi",
                    Toasty.LENGTH_SHORT,
                    true
                ).show()
                val intent = Intent(this@PayInfoActivity, BookingActivity::class.java)
                startActivity(intent)
                finish()
            },
            onError = { _, _ ->
                progressDialog.dismiss()
                Toasty.error(
                    this@PayInfoActivity,
                    getString(R.string.try_again),
                    Toasty.LENGTH_SHORT,
                    true
                ).show()
            }
        )
    }

    private fun getCardData(): CardInfo {

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString("card_number", cardNumber.text.toString())
            putString("card_month", cardExpMonth.text.toString())
        }.apply()

        val cardsNumber: String = cardNumber.text.toString()
        val cardExpMonths: String = cardExpMonth.text.toString()

        return CardInfo(
            number = cardsNumber,
            cardHolder = "",
            expirationDate = cardExpMonths,
            securityNumber = "",
        )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
}
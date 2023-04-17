package com.example.sayidakramuchun.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sayidakramuchun.R
import com.example.sayidakramuchun.databinding.FragmentHomeBinding
import com.example.sayidakramuchun.models.MyHelp
import com.example.sayidakramuchun.retrofit.ApiCient
import com.example.sayidakramuchun.utils.MySharedPreference
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.gcm.Task
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)

        askPermission(
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            //all permissions already granted or just granted

            binding.myRoot.setOnLongClickListener {
                val vibrator =
                    requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            1000,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(1000)
                }

//                sendSms()
                myLocation()
                true
            }

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(binding.root.context)
                    .setMessage("Ruxsat bermasangiz ilova to'gri ishlamaydi")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info -> {
                val dialog = AlertDialog.Builder(binding.root.context).create()
                dialog.setTitle("Dastur haqida")
                dialog.setMessage("Bu dastur MVP bo'lib SAYDAKRAM ning g'oyasi bo'yicha ishlab chiqilgan. \nSaydakram raqami: 90 783 23 31")
                dialog.show()
            }
            R.id.menu_edit -> {
                MySharedPreference.name = "null"
                MySharedPreference.number = "null"
                MySharedPreference.numberHelp = "null"
                findNavController().popBackStack()
                findNavController().navigate(R.id.signFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendSms(location:String) {
        val phoneNumber = MySharedPreference.numberHelp // Telefon numarası burada belirtilmelidir
        val message =
            "${MySharedPreference.name} ga yordam kerak, ${MySharedPreference.number}.\nhttps://www.google.com/maps/search/?api=1&query=${location}" // Gönderilecek mesaj burada belirtilmelidir

        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        binding.tvInfo.text = "Yordam so'rovi jo'natildi"
    }

    @SuppressLint("MissingPermission")
    fun myLocation() {
//        val locationManager =
//            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//
//        // Konum alınır
//        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        val latitude = location?.latitude
//        val longitude = location?.longitude
//        Log.d("LOCATION", "Lat: $latitude, Lng: $longitude")

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(binding.root.context)
        val locationTask = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { it: Location ->
            if (it != null) {
                //We have a location
                Log.d(TAG, "getLastLocation: ${it.toString()}")
                Log.d(TAG, "getLastLocation: ${it.latitude}")
                Log.d(TAG, "getLastLocation: ${it.longitude}")

                sendSms("${it.latitude},${it.longitude}")
                requestRetrofit(MyHelp(1, MySharedPreference.name, it.latitude.toString(), it.longitude.toString(), MySharedPreference.number, MySharedPreference.sana.toInt()))

            } else {
                Log.d(
                    TAG,
                    "getLastLocation: location was null,,,,,,,,,,,,,,,,,,,..............."
                )
            }
        }
        locationTask.addOnFailureListener {
            Log.d(TAG, "getLastLocation: ${it.message}")
            sendSms(" aniqlanmadi ")
        }

    }

    fun requestRetrofit(myHelp:MyHelp){
        ApiCient.apiService.createUser(myHelp)
            .enqueue(object :Callback<MyHelp>{
                override fun onResponse(call: Call<MyHelp>, response: Response<MyHelp>) {
                    if (response.isSuccessful){
                        binding.tvInfo.text = "Bazaga xabar yuborildi"
                    }
                }

                override fun onFailure(call: Call<MyHelp>, t: Throwable) {
                    Toast.makeText(context, "Internet bilan muammo", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
package com.example.sayidakramuchun.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.sayidakramuchun.R
import com.example.sayidakramuchun.databinding.FragmentSignBinding
import com.example.sayidakramuchun.utils.MySharedPreference

class SignFragment : Fragment() {
    private val binding by lazy { FragmentSignBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        MySharedPreference.init(binding.root.context)
        if (MySharedPreference.name!="null" || MySharedPreference.number != "null"){
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            return binding.root
        }

        binding.apply {
            btnNext.setOnClickListener {
                if (edtName.text.isNotBlank()){
                    if (edtNumber.text.isNotBlank()){
                        if (edtNumberHelp.text.isNotBlank()){
                            if (edtAge.text.isNotBlank()){
                                MySharedPreference.name = edtName.text.toString()
                                MySharedPreference.number = edtNumber.text.toString()
                                MySharedPreference.numberHelp = edtNumberHelp.text.toString()
                                MySharedPreference.sana = edtAge.text.toString()
                                findNavController().popBackStack()
                                findNavController().navigate(R.id.homeFragment)
                            }else{
                                Toast.makeText(
                                    context,
                                    "Tug'ilgan sanangizni to'g'ri yozing",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }else{
                        Toast.makeText(context, "Telefon raqam kiriting", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, "Ismingizni kiriting", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

}
package com.example.wooda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wooda.databinding.FragmentChattingBinding
import com.example.wooda.databinding.FragmentMypageBinding
import java.net.URL


class MypageFragment : Fragment() {

    lateinit var binding : FragmentMypageBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
   ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        var id = arguments?.getString("id")
//        // 프로필 변경을 클릭했을 경우
//        binding.btnProfileImage.setOnClickListener {
//
//        }
//
//        // 나의 게시물 버튼을 클릭했을 경우
//        binding.btnBoarder.setOnClickListener {
//
//        }
//
//        // 연인등록을 클릭했을 경우
        binding.btnCouple.setOnClickListener {

            var intent = Intent(context, CoupleActivity::class.java)

            intent.putExtra("id", "${id}")
            startActivity(intent)

        }

        binding.imageView2.setImageResource(R.drawable.junni)

//
        // 공지사항 버튼을 클릭했을 경우
        binding.btnNotice.setOnClickListener {

            var intent = Intent(context, NoticeActivity::class.java)

            startActivity(intent)

        }

        binding.btnLogout.setOnClickListener {
            activity?.let { it1 -> MySharedPreferences.clearUser(it1.applicationContext) }
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
//
//        // 고객센터 버튼을 클릭했을 경우
//        binding.btnService.setOnClickListener {
//
//        }
//
        // 회원정보 버튼을 클릭했을 경우
        binding.btnProfile.setOnClickListener {
            var intent = Intent(context, ProfileActivity::class.java)

            intent.putExtra("id", "${id}")
            startActivity(intent)
        }

        return binding.root
    }

}
package com.example.playlistmaker.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R

class IntentFactory {
    companion object {
        fun getShareIntent(context: Context): Intent {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.yandex_android_link))
                type = context.resources.getString(R.string.intent_text_type)
            }
            return Intent.createChooser(intent, context.resources.getString(R.string.share_with))
        }

        fun getSupportIntent(context: Context): Intent {
            return Intent(Intent.ACTION_SENDTO).apply {
                data = context.resources.getString(R.string.intent_email_type).toUri()
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(context.resources.getString(R.string.yandex_android_link))
                )
                putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.letter_topic))
                putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.letter_text))
            }
        }

        fun getAgreementIntent(context: Context): Intent {
            return Intent(
                Intent.ACTION_VIEW,
                context.resources.getString(R.string.yandex_agreement_link).toUri()
            )
        }
    }
}
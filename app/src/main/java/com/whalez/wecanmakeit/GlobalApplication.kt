package com.whalez.wecanmakeit

import android.app.Application
import com.kakao.auth.*

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // SDK 초기화
        KakaoSDK.init(object : KakaoAdapter() {
            override fun getApplicationConfig(): IApplicationConfig {
                return IApplicationConfig { this@GlobalApplication }
            }

            override fun getSessionConfig(): ISessionConfig {
                return object: ISessionConfig {
                    override fun isSaveFormData(): Boolean {
                        return false
                    }

                    override fun getAuthTypes(): Array<AuthType> {
                        return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                    }

                    override fun isSecureMode(): Boolean {
                        return false
                    }

                    override fun getApprovalType(): ApprovalType? {
                        return ApprovalType.INDIVIDUAL
                    }

                    override fun isUsingWebviewTimer(): Boolean {
                        return false
                    }

                }
            }
        })
    }
}
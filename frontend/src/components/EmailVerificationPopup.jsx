"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import {X, Mail, Check, Info} from "lucide-react"
import {sendVerificationEmail} from "@/http/userAPI.js";

export default function EmailVerificationPopup() {
    const [isVisible, setIsVisible] = useState(true)
    const [isLoading, setIsLoading] = useState(false)
    const [emailSent, setEmailSent] = useState(false)

    const handleSendVerification = async () => {
        setIsLoading(true)
        await sendVerificationEmail()
        setIsLoading(false)
        setEmailSent(true)
    }

    const handleClose = () => {
        setIsVisible(false)
    }

    if (!isVisible) return null

    return (
        <div className="fixed top-4 left-1/2 transform -translate-x-1/2 z-50 w-full max-w-md px-4">
            <div className="bg-card border border-border rounded-lg shadow-lg p-4 animate-in slide-in-from-top-2 duration-300">
                <div className="flex items-start gap-3">
                    <div className="flex-shrink-0">
                        <Info className="h-5 w-5 mt-0.5" />
                    </div>

                    <div className="flex-1 min-w-0 text-left">
                        <h3 className="text-sm font-semibold text-card-foreground">Email Not Verified</h3>
                        <p className="text-sm text-muted-foreground mt-1">
                            Please check your inbox and verify your email address to access all features.
                        </p>

                        <div className="flex items-center gap-2 mt-3">
                            <Button
                                onClick={handleSendVerification}
                                disabled={isLoading || emailSent}
                                size="sm"
                                className="h-8"
                                variant={emailSent ? "secondary" : "default"}
                            >
                                {isLoading ? (
                                    <>
                                        <div className="animate-spin rounded-full h-3 w-3 border-b-2 border-primary-foreground mr-2" />
                                        Sending...
                                    </>
                                ) : emailSent ? (
                                    <>
                                        <Check className="h-3 w-3 mr-2" />
                                        Email Sent
                                    </>
                                ) : (
                                    <>
                                        <Mail className="h-3 w-3 mr-2" />
                                        Send Verification
                                    </>
                                )}
                            </Button>
                        </div>
                    </div>

                    <Button variant="ghost" size="sm" onClick={handleClose} className="h-6 w-6 p-0 hover:bg-muted">
                        <X className="h-4 w-4" />
                        <span className="sr-only">Close notification</span>
                    </Button>
                </div>
            </div>
        </div>
    )
}

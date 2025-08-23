import {useContext, useState} from "react";
import {toast} from "sonner";
import errorToast from "@/utils/errorToast.jsx";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.js";
import {Label} from "@radix-ui/react-label";
import {Input} from "@/components/ui/input.js";
import {Button} from "@/components/ui/button.js";
import {CreditCard} from "lucide-react";
import {becomeSeller, updateSeller} from "@/http/sellerAPI.js";
import {Context} from "@/main.jsx";
import {observer} from "mobx-react-lite";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group.js";
import {useNavigate} from "react-router-dom";
import {HOME_ROUTE} from "@/utils/consts.js";

const YooKassaIcon = () => (
    <img
        src="https://logo-teka.com/wp-content/uploads/2025/07/yookassa-sign-logo.svg"
        alt="YooKassa"
        width="20"
        height="20"
        className="object-contain"
    />
)

function BecomeSeller({ mode = "create", initialMethod, initialDestination, onSuccess }) {
    const {user} = useContext(Context)

    const [payoutMethod, setPayoutMethod] = useState(initialMethod || "YOOMONEY_WALLET")
    const [payoutDestination, setPayoutDestination] = useState(initialDestination || "")
    const [isSubmitting, setIsSubmitting] = useState(false)

    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!payoutDestination.trim()) {
            toast.error("Error", { description: "Please enter your payout destination" })
            return
        }

        setIsSubmitting(true)

        try {
            if (mode === "create") {
                await becomeSeller(parseInt(user.user.sub), payoutMethod, payoutDestination)
                user.user.role = "SELLER"
                toast.success("Congratulations!", {
                    description: "You have access to seller functionality now.",
                })
                navigate(HOME_ROUTE)
                return
            } else {
                await updateSeller(payoutMethod, payoutDestination)
                toast.success("Updated!", {
                    description: "Your payout info has been updated.",
                })
            }

            onSuccess?.()
        } catch (e) {
            errorToast(e)
        } finally {
            setIsSubmitting(false)
        }
    }

    const getDestinationPlaceholder = () => {
        return payoutMethod === "YOOMONEY_WALLET" ? "Enter your YooMoney wallet ID" : "Enter your bank card number"
    }

    const getDestinationLabel = () => {
        return payoutMethod === "YOOMONEY_WALLET" ? "YooMoney Wallet ID" : "Bank Card Number"
    }

    return (
        <div className={
            mode === "create"
                ? "min-h-screen flex items-center justify-center p-4 text-left"
                : "w-full"
        }
        >
            <Card
                className={
                    mode === "create"
                        ? "w-full max-w-md"
                        : "w-full"
                }
            >
                <CardHeader className="text-center">
                    <CardTitle className="text-2xl font-bold">
                        {mode === "create" ? "Become a Seller" : "Update Payout Settings"}
                    </CardTitle>
                    <CardDescription>
                        {mode === "create"
                            ? "Join our marketplace and start selling your products"
                            : "Manage your payout preferences"}
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="space-y-3">
                            <Label className="text-sm font-medium">Payout Method</Label>
                            <RadioGroup
                                value={payoutMethod}
                                onValueChange={(value) => {
                                    setPayoutMethod(value)
                                    setPayoutDestination("")
                                }}
                                className="grid grid-cols-1 gap-3"
                            >
                                <div className="flex items-center space-x-2 border rounded-lg p-3 hover:bg-accent/50 transition-colors">
                                    <RadioGroupItem value="YOOMONEY_WALLET" id="YOOMONEY_WALLET" />
                                    <YooKassaIcon />
                                    <Label htmlFor="YOOMONEY_WALLET" className="flex-1 cursor-pointer">
                                        YooMoney Wallet
                                    </Label>
                                </div>
                                <div className="flex items-center space-x-2 border rounded-lg p-3 hover:bg-accent/50 transition-colors">
                                    <RadioGroupItem value="BANK_CARD" id="BANK_CARD" />
                                    <CreditCard className="w-5 h-5 text-muted-foreground" />
                                    <Label htmlFor="BANK_CARD" className="flex-1 cursor-pointer">
                                        Bank Card
                                    </Label>
                                </div>
                            </RadioGroup>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="destination" className="text-sm font-medium">
                                {getDestinationLabel()}
                            </Label>
                            <Input
                                id="destination"
                                type={payoutMethod === "BANK_CARD" ? "text" : "text"}
                                placeholder={getDestinationPlaceholder()}
                                value={payoutDestination}
                                onChange={(e) => setPayoutDestination(e.target.value)}
                                className="w-full"
                                maxLength={payoutMethod === "BANK_CARD" ? 19 : undefined}
                            />
                            {payoutMethod === "BANK_CARD" && (
                                <p className="text-xs text-muted-foreground">Enter your 16-digit card number</p>
                            )}
                        </div>

                        <Button type="submit" className="w-full" disabled={isSubmitting}>
                            {isSubmitting
                                ? "Submitting..."
                                : mode === "create"
                                    ? "Submit"
                                    : "Update"}
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}

export default observer(BecomeSeller)
import {Card, CardContent, CardFooter, CardHeader, CardTitle} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Button} from "@/components/ui/button"
import {useLocation, useNavigate, useSearchParams} from "react-router-dom"
import {HOME_ROUTE, LOGIN_ROUTE, REGISTRATION_ROUTE} from "../utils/consts"
import {
    changePassword,
    login,
    registration,
    sendPasswordResetEmail,
    validatePasswordResetToken
} from "@/http/userAPI.js"
import {useContext, useEffect, useState} from "react"
import {observer} from "mobx-react-lite"
import {Context} from "@/main.jsx"
import {toast} from "sonner"
import errorToast from "@/utils/errorToast.jsx"
import {Eye, EyeOff} from "lucide-react"

function Auth() {
    const {user} = useContext(Context)
    const location = useLocation()
    const navigate = useNavigate()
    const [searchParams] = useSearchParams()

    const initialMode = location.pathname === LOGIN_ROUTE ? "login" : "signup"

    const [mode, setMode] = useState(initialMode)
    const [showPassword, setShowPassword] = useState(false)
    const [showConfirmPassword, setShowConfirmPassword] = useState(false)

    const [formData, setFormData] = useState({
        email: "",
        username: "",
        password: "",
        confirmPassword: "",
    })

    useEffect(() => {
        const token = searchParams?.get("passwordReset")
        if (!token) {
            return
        }

        async function validateToken() {
            try {
                await validatePasswordResetToken(token);
            } catch (e) {
                errorToast(e)
                navigate(LOGIN_ROUTE)
            }
        }

        validateToken().then(() => {
            setMode("reset")
        })
    }, []);

    useEffect(() => {
        if (location.pathname === LOGIN_ROUTE) {
            setMode("login")
        } else {
            setMode("signup")
        }
    }, [location.pathname])

    const updateField = (name, value) => {
        setFormData((prev) => ({...prev, [name]: value}))
    }

    const handleSubmit = async () => {
        try {
            if (mode === "login") {
                const data = await login(formData.email, formData.password)
                user.setUser(data)
                user.setIsAuth(true)
                navigate(HOME_ROUTE)
            } else if (mode === "signup") {
                const data = await registration(formData.username, formData.email, formData.password)
                user.setUser(data)
                user.setIsAuth(true)
                navigate(HOME_ROUTE)
            } else if (mode === "forgot") {
                try {
                    await sendPasswordResetEmail(formData.email)

                    toast.success("Password reset link sent", {
                        description: "If an account with this email exists, you will receive a link.",
                    })
                } catch (e) {
                    errorToast(e)
                }

            } else if (mode === "reset") {
                if (formData.password !== formData.confirmPassword) {
                    toast.error("Passwords do not match")
                } else {
                        await changePassword(formData.password, searchParams.get("passwordReset"))
                        toast.success("Password changed successfully")
                        setMode("login")
                }
            }
        } catch (e) {
            errorToast(e)
        }
    }

    const fieldConfig = {
        login: [
            {name: "email", label: "Email", type: "email"},
            {name: "password", label: "Password", type: "password"},
        ],
        signup: [
            {name: "email", label: "Email", type: "email"},
            {name: "username", label: "Username", type: "text"},
            {name: "password", label: "Password", type: "password"},
        ],
        forgot: [{name: "email", label: "Email", type: "email"}],
        reset: [
            {name: "password", label: "New Password", type: "password"},
            {name: "confirmPassword", label: "Confirm Password", type: "password"},
        ],
    }

    const titles = {
        login: "Sign In",
        signup: "Sign Up",
        forgot: "Forgot Password",
        reset: "Reset Password",
    }

    const primaryButtonLabels = {
        login: "Login",
        signup: "Sign Up",
        forgot: "Send Reset Link",
        reset: "Change Password",
    }

    const secondaryActions = {
        login: [{label: "Don't have an account?", onClick: () => navigate(REGISTRATION_ROUTE)}],
        signup: [{label: "Already have an account?", onClick: () => navigate(LOGIN_ROUTE)}],
        forgot: [{label: "Back to Login", onClick: () => {
                navigate(LOGIN_ROUTE)
                setMode("login")
            }}],
        reset: [{label: "Back to Login", onClick: () => {
                navigate(LOGIN_ROUTE)
                setMode("login")
            }}],
    }

    const renderPasswordField = (field, isConfirmPassword = false) => {
        const showPasswordState = isConfirmPassword ? showConfirmPassword : showPassword
        const setShowPasswordState = isConfirmPassword ? setShowConfirmPassword : setShowPassword

        return (
            <div className="space-y-2" key={field.name}>
                <Label htmlFor={field.name}>{field.label}</Label>
                <div className="relative">
                    <Input
                        id={field.name}
                        type={showPasswordState ? "text" : "password"}
                        value={formData[field.name]}
                        onChange={(e) => updateField(field.name, e.target.value)}
                        className="pr-10"
                        autoComplete="off"
                    />
                    <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-muted/50 transition-colors duration-200"
                        onClick={() => setShowPasswordState(!showPasswordState)}
                    >
                        {showPasswordState ? (
                            <EyeOff
                                className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors duration-200"/>
                        ) : (
                            <Eye
                                className="h-4 w-4 text-muted-foreground hover:text-foreground transition-colors duration-200"/>
                        )}
                        <span className="sr-only">{showPasswordState ? "Hide password" : "Show password"}</span>
                    </Button>
                </div>
            </div>
        )
    }

    return (
        <div className="w-full min-h-screen flex items-center justify-center bg-background/80 px-4 pt-20">
            <Card className="w-full max-w-md p-4 -mt-30">
                <CardHeader>
                    <CardTitle className="text-2xl text-center">{titles[mode]}</CardTitle>
                </CardHeader>

                <CardContent className="space-y-4">
                    {fieldConfig[mode].map((field) => {
                        if (field.type === "password") {
                            const isConfirmPassword = field.name === "confirmPassword"
                            return renderPasswordField(field, isConfirmPassword)
                        }

                        return (
                            <div className="space-y-2" key={field.name}>
                                <Label htmlFor={field.name}>{field.label}</Label>
                                <Input
                                    id={field.name}
                                    type={field.type}
                                    value={formData[field.name]}
                                    onChange={(e) => updateField(field.name, e.target.value)}
                                    autoComplete="off"
                                />
                            </div>
                        )
                    })}

                    {/* Show "Don't remember password?" link below password input in login mode */}
                    {mode === "login" && (
                        <div className="flex justify-start">
                            <Button
                                variant="link"
                                className="h-auto p-0 text-sm text-muted-foreground hover:text-foreground"
                                onClick={() => setMode("forgot")}
                            >
                                Don't remember password?
                            </Button>
                        </div>
                    )}
                </CardContent>

                <CardFooter className="flex flex-col gap-2">
                    <Button className="w-full" onClick={handleSubmit}>
                        {primaryButtonLabels[mode]}
                    </Button>

                    {secondaryActions[mode].map((action, idx) => (
                        <Button key={idx} variant="link" className="w-full text-sm text-muted-foreground"
                                onClick={action.onClick}>
                            {action.label}
                        </Button>
                    ))}
                </CardFooter>
            </Card>
        </div>
    )
}

export default observer(Auth)

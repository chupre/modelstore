import AppRouter from './components/AppRouter';
import {useSearchParams} from "react-router-dom"
import { ThemeProvider } from './components/ui/theme-provider';
import Navbar from './components/Navbar'
import './App.css'
import { Toaster } from "@/components/ui/sonner"
import { observer } from "mobx-react-lite";
import {useContext, useEffect, useState} from "react";
import {refresh} from "@/http/userAPI.js";
import {Context} from "@/main.jsx";
import Loading from "@/components/Loading.jsx";
import EmailVerificationPopup from "@/components/EmailVerificationPopup.jsx";

function App() {
    const {user} = useContext(Context);
    const [loading, setLoading] = useState(true);
    const [showEmailVerification, setShowEmailVerification] = useState(false)
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            setLoading(false);
            return;
        }

        const hasVerificationParam = !!searchParams.get("verification");

        refresh().then((data) => {
            user.setUser(data)
            user.setIsAuth(true)

            if (!data.verified && !hasVerificationParam) {
                setShowEmailVerification(true)
            }
        }).finally(() => {setLoading(false)});
    }, [])

    if (loading) {
        return <Loading/>
    }

    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
                <Navbar></Navbar>
                <AppRouter/>
                {showEmailVerification && <EmailVerificationPopup onClose={() => setShowEmailVerification(false)}/>}
                <Toaster position="top-center" theme={"dark"} toastOptions={{
                    classNames: {
                        toast: "items-start text-left"
                }
                }}/>
        </ThemeProvider>
    )
}

export default observer(App)

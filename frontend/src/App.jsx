import AppRouter from './components/AppRouter';
import { BrowserRouter } from "react-router-dom"
import { ThemeProvider } from './components/ui/theme-provider';
import Navbar from './components/Navbar'
import './App.css'
import {Toaster} from "sonner";
import { observer } from "mobx-react-lite";
import {useContext, useEffect, useState} from "react";
import {refresh} from "@/http/userAPI.js";
import {Context} from "@/main.jsx";
import { Spinner } from '@/components/ui/shadcn-io/spinner';

function App() {
    const {user} = useContext(Context);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            setLoading(false);
            return;
        }

        refresh().then((data) => {
            user.setUser(data)
            user.setIsAuth(true)
        }).finally(() => {setLoading(false)});
    }, [])

    if (loading) {
        return (
            <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
                <div className="w-screen h-screen flex items-center justify-center">
                    <Spinner />
                </div>
            </ThemeProvider>
        )
    }

    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <BrowserRouter>
                <Navbar></Navbar>
                <AppRouter/>
                <Toaster position="top-center" theme={"dark"}/>
            </BrowserRouter>
        </ThemeProvider>
    )
}

export default observer(App)

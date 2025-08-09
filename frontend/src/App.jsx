import AppRouter from './components/AppRouter';
import { BrowserRouter } from "react-router-dom"
import { ThemeProvider } from './components/ui/theme-provider';
import Navbar from './components/Navbar'
import './App.css'
// import {Toaster} from "sonner";
//import {Toaster} from "./components/ui/Toaster";
import { Toaster } from "@/components/ui/sonner"
import { observer } from "mobx-react-lite";
import {useContext, useEffect, useState} from "react";
import {refresh} from "@/http/userAPI.js";
import {Context} from "@/main.jsx";
import Loading from "@/components/Loading.jsx";

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
        return <Loading/>
    }

    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <BrowserRouter>
                <Navbar></Navbar>
                <AppRouter/>
                <Toaster position="top-center" theme={"dark"} toastOptions={{
                    classNames: {
                        toast: "items-start text-left"
                }
                }}/>
            </BrowserRouter>
        </ThemeProvider>
    )
}

export default observer(App)

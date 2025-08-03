import AppRouter from './components/AppRouter';
import { BrowserRouter } from "react-router-dom"
import { ThemeProvider } from './components/ui/theme-provider';
import Navbar from './components/Navbar'
import './App.css'
import {Toaster} from "sonner";

function App() {
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

export default App

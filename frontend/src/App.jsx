import AppRouter from './components/AppRouter';
import { BrowserRouter } from "react-router-dom"
import { ThemeProvider } from './components/ui/theme-provider';
import Navbar from './components/Navbar'
import './App.css'

function App() {
    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <BrowserRouter>
                <Navbar></Navbar>
                <AppRouter />
            </BrowserRouter>
        </ThemeProvider>
    )
}

export default App

import { observer } from "mobx-react-lite";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useContext } from "react";
import { Context } from '../main';
import { useNavigate } from "react-router-dom";
import { HOME_ROUTE, LOGIN_ROUTE, REGISTRATION_ROUTE, STORE_ROUTE } from "../utils/consts";
import {LogOut} from "lucide-react";

const Navbar = observer(() => {
    const { user } = useContext(Context);
    const navigate = useNavigate();

    const logOut = () => {
        user.setUser({});
        user.setIsAuth(false);
        localStorage.removeItem('token');
    }

    return (
        <header className="fixed top-0 left-0 w-full z-50 flex items-center justify-between px-6 py-4 border-b bg-background">
            <div className="flex items-center space-x-4">
                <div
                    className="flex items-center space-x-2 cursor-pointer"
                    onClick={() => navigate(HOME_ROUTE)}
                >
                    <img
                        src="/images/logo.png"
                        alt="3D Store Logo"
                        className="w-8 h-8 object-cover rounded-full"
                    />
                    <span className="text-xl font-bold">3D Store</span>
                </div>
                <div className="flex items-center">
                    <Button variant="ghost" onClick={() => navigate(HOME_ROUTE)}>Home</Button>
                    <Button variant="ghost" onClick={() => navigate(STORE_ROUTE)}>Catalog</Button>
                </div>
            </div>

            {user.isAuth ? (
                <div className="flex items-center space-x-3">
                    <span className="text-sm font-medium">John Doe</span>
                    <Avatar>
                        <AvatarImage src="https://github.com/shadcn.png" alt="User" />
                        <AvatarFallback>JD</AvatarFallback>
                    </Avatar>
                    <Button
                        type="submit"
                        variant="secondary"
                        size="icon"
                        onClick={() => {logOut()}}
                    >
                        <LogOut className="w-4 h-4" />
                    </Button>
                </div>
            ) : (
                <div className="flex items-center space-x-3">
                    <Button
                        onClick={() => {
                            navigate(LOGIN_ROUTE);
                        }}
                    >
                        Login
                    </Button>
                    <Button
                        variant="outline"
                        onClick={() => {
                            navigate(REGISTRATION_ROUTE);
                        }}
                    >
                        Sign Up
                    </Button>
                </div>
            )}
        </header>
    );
});

export default Navbar;
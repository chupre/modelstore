import { observer } from "mobx-react-lite";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {useContext, useEffect} from "react";
import { Context } from '../main';
import { useNavigate } from "react-router-dom";
import {ADMIN_ROUTE, HOME_ROUTE, LOGIN_ROUTE, PROFILE_ROUTE, REGISTRATION_ROUTE} from "../utils/consts";
import {LogOut, User} from "lucide-react";
import NavbarCatalogButton from "@/components/NavbarCatalogButton.jsx";
import {fetchCategories} from "@/http/productAPI.js";
import ShoppingCartButton from "@/components/ShoppingCartButton.jsx";
import {fetchProfile} from "@/http/userAPI.js";

const Navbar = observer(() => {
    const { user, product } = useContext(Context);
    const navigate = useNavigate();

    useEffect(() => {
        fetchCategories().then((res) => {
            product.setCategories(res.data.content);
        })

        if (user.isAuth) {
            fetchProfile(user.user.sub).then((res) => {
                user.setProfile(res.data)
            })
        }
    }, [user.isAuth])

    const logOut = () => {
        user.setUser({});
        user.setIsAuth(false);
        localStorage.removeItem('token');
        navigate(HOME_ROUTE);
    }

    return (
        <header className="fixed top-0 left-0 w-full z-50 flex items-center justify-between px-6 py-2 border-b bg-background/50 backdrop-blur-lg">
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
                    <NavbarCatalogButton categories={product.categories} />
                    {user.user?.role === "ADMIN" &&
                        <Button variant="ghost" onClick={() => navigate(ADMIN_ROUTE)}>
                            Admin
                        </Button>
                    }
                </div>
            </div>

            {user.isAuth ? (
                <div className="flex items-center space-x-3">
                    <ShoppingCartButton/>
                    <Button
                        type="submit"
                        variant="ghost"
                        size="icon"
                        onClick={() => {logOut()}}
                    >
                        <LogOut className="w-4 h-4" />
                    </Button>
                    <Avatar
                        onClick={() => navigate(PROFILE_ROUTE + '/' + user.user.sub)}
                        className="hover:cursor-pointer"
                    >
                        <AvatarImage src={`${import.meta.env.VITE_API_URL}${user.profile.avatarUrl}`} alt="User" />
                        <AvatarFallback>
                            <User/>
                        </AvatarFallback>
                    </Avatar>
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
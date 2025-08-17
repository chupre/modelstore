import Auth from "./pages/Auth"
import Home from "./pages/Home"
import Store from "./pages/Store"
import {
    HOME_ROUTE,
    LOGIN_ROUTE,
    PRODUCT_ROUTE,
    PROFILE_ROUTE,
    REGISTRATION_ROUTE,
    STORE_ROUTE
} from "./utils/consts"
import ProductPage from "@/pages/ProductPage.jsx";
import ProfilePage from "@/pages/ProfilePage.jsx";

export const authRoutes = [

]

export const publicRoutes = [
    {
        path: HOME_ROUTE,
        Component: Home
    },
    {
        path: LOGIN_ROUTE,
        Component: Auth
    },
    {
        path: REGISTRATION_ROUTE,
        Component: Auth
    },
    {
        path: STORE_ROUTE,
        Component: Store
    },
    {
        path: PRODUCT_ROUTE + '/:id',
        Component: ProductPage
    },
    {
        path: PROFILE_ROUTE + '/:id',
        Component: ProfilePage
    }
]

import Auth from "./pages/Auth"
import Home from "./pages/Home"
import Store from "./pages/Store"
import { HOME_ROUTE, LOGIN_ROUTE, REGISTRATION_ROUTE, STORE_ROUTE } from "./utils/consts"

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
]
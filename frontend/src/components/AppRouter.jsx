import {Routes, Route, Navigate} from 'react-router-dom'
import {publicRoutes} from '../routes'
import {ADMIN_ROUTE, HOME_ROUTE, SELLER_ROUTE} from '../utils/consts';
import {useContext} from 'react';
import {Context} from '../main';
import AdminPage from "@/pages/Admin.jsx";
import {observer} from "mobx-react-lite";
import SellerPage from "@/pages/SellerPage.jsx";

function AppRouter() {
    const {user} = useContext(Context);

    return (
        <Routes>
            {publicRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={<Component/>}/>
            ))}

            {user.isAuth && user.user.role === 'ADMIN' && (
                <Route path={ADMIN_ROUTE} element={<AdminPage/>}/>
            )}

            {user.isAuth && user.user.verified && (
                <Route path={SELLER_ROUTE} element={<SellerPage/>}/>
            )}
            <Route path="*" element={<Navigate to={HOME_ROUTE} replace/>}/>
        </Routes>
    );
}

export default observer(AppRouter);

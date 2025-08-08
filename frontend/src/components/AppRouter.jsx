import {Routes, Route, Navigate} from 'react-router-dom'
import {authRoutes, publicRoutes} from '../routes'
import {ADMIN_ROUTE, HOME_ROUTE} from '../utils/consts';
import {useContext} from 'react';
import {Context} from '../main';
import AdminPage from "@/pages/Admin.jsx";

export default function AppRouter() {
    const {user} = useContext(Context);

    return (
        <Routes>
            {user.isAuth &&
                authRoutes.map(({path, Component}) => (
                    <Route key={path} path={path} element={<Component/>}/>
                ))}

            {publicRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={<Component/>}/>
            ))}

            {user.isAuth && user.user.role === 'ADMIN' && (
                <Route path={ADMIN_ROUTE} element={<AdminPage/>}/>
            )}

            <Route path="*" element={<Navigate to={HOME_ROUTE} replace/>}/>
        </Routes>
    );
}

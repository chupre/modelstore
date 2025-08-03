import { Routes, Route, Navigate } from 'react-router-dom'
import { authRoutes, publicRoutes } from '../routes'
import { HOME_ROUTE } from '../utils/consts';
import { useContext } from 'react';
import { Context } from '../main';

export default function AppRouter() {
  const {user} = useContext(Context);
  
  return (
    <Routes>
      {user.isAuth &&
        authRoutes.map(({ path, Component }) => (
          <Route key={path} path={path} element={<Component />} />
        ))}

      {publicRoutes.map(({ path, Component }) => (
        <Route key={path} path={path} element={<Component />} />
      ))}
      <Route path="*" element={<Navigate to={HOME_ROUTE} replace />} />
    </Routes>
  );
}

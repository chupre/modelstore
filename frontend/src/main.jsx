import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import React, { createContext } from 'react';
import './index.css'
import App from './App.jsx'
import UserStore from './store/UserStore.js'
import ProductStore from './store/ProductStore.js';

const rootElement = document.getElementById('root')
if (!rootElement) throw new Error("Root element not found")

export const Context = createContext(null)

createRoot(rootElement).render(
  <Context.Provider value={{
    user: new UserStore(),
    product: new ProductStore()
  }}>
    <StrictMode>
      <App />
    </StrictMode>
  </Context.Provider>
)

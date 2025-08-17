import {$authHost, $host} from "./index";
import {jwtDecode} from 'jwt-decode';

export const registration = async (username, email, password) => {
    await $host.post('/users', {username, email, password})
    const {data} = await $host.post('/auth/login', {email, password})
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}

export const login = async (email, password) => {
    const {data} = await $host.post('/auth/login', {email, password})
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}

export const refresh = async () => {
    const {data} = await $host.post('/auth/refresh')
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}

export const sendVerificationEmail = async () => {
    return $authHost.post('/users/verification')
}

export const verify = async (token) => {
    return $authHost.get(`users/verification/${token}`)
}

export const sendPasswordResetEmail = async (email) => {
    return $authHost.post(`users/passwordReset`, {email})
}

export const validatePasswordResetToken = async (token) => {
    return $authHost.get(`users/passwordReset?token=${token}`)
}

export const changePassword = async (newPassword, token) => {
    return $authHost.post(`users/passwordReset/confirm`, {token, newPassword})
}

export const fetchProfile = async (id) => {
    return $host.get(`users/${id}/profile`)
}

export const updateProfile = async (id, profileFormData) => {
    return await $authHost.put(`users/${id}/profile`, profileFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}
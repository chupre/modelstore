import {makeAutoObservable} from 'mobx'

export default class UserStore {
    constructor() {
        this._isAuth = false;
        this._user = {};
        this._profile = {};
        makeAutoObservable(this);
    }

    setIsAuth(bool) {
        this._isAuth = bool;  
    }
    setUser(user) {
        this._user = user;
    }
    setProfile(profile) {
        this._profile = profile;
    }

    get isAuth() {
        return this._isAuth;
    }
    get user() {
        return this._user;
    }
    get profile() {
        return this._profile;
    }
}
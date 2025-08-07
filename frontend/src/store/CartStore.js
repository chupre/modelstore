import {makeAutoObservable} from "mobx";

export default class CartStore {
    constructor() {
        this._cart = null;
        makeAutoObservable(this)
    }

    get cart() {
        return this._cart;
    }

    setCart(cart) {
        this._cart = cart;
    }

    addCartItem(item) {
        if (!this._cart) {
            this._cart = { cartItems: [] };
        }

        if (!this.isInCart(item.product.id)) {
            this._cart.cartItems.push(item);
        }
    }

    removeCartItem(productId) {
        if (!this._cart) return;

        this._cart.cartItems = this._cart.cartItems.filter(i => i.product.id !== productId);
    }

    clearCart() {
        if (!this._cart) return;

        this._cart.cartItems = [];
    }

    isInCart(productId) {
        if (!this._cart) {
            return false;
        }

        return this._cart.cartItems.some(i => i.product.id === productId);
    }
}
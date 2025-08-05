import { makeAutoObservable } from 'mobx'

export default class ProductStore {
    constructor() {
        this._categories = [];
        this._products = [];
        this._currentPage = 0;
        this._totalPages = 0;
        this._limit = 6;

        makeAutoObservable(this);
    }
    setCategories(categories) {
        this._categories = categories;
    }
    setProducts(products) {
        this._products = products;
    }
    get categories() {
        return this._categories;
    }

    get products() {
        return this._products;
    }
    get currentPage() {
        return this._currentPage;
    }

    setCurrentPage(value) {
        this._currentPage = value;
    }

    get totalPages() {
        return this._totalPages;
    }

    setTotalPages(value) {
        this._totalPages = value;
    }

    get limit() {
        return this._limit;
    }

    setLimit(value) {
        this._limit = value;
    }
}
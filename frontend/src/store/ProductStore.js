import {makeAutoObservable, runInAction} from 'mobx'
import {fetchProducts as fetchProductsApi, fetchProductsWithUserLike as fetchProductsWithUserLikeApi} from "@/http/productAPI.js";

export default class ProductStore {
    constructor() {
        this._categories = [];
        this._products = [];
        this._currentPage = 0;
        this._totalPages = 0;
        this._limit = 6;

        this._search = "";
        this._sortBy = "";
        this._priceRange = [0, 200];
        this._categoryId = "";

        makeAutoObservable(this);
    }
    async fetchProducts(isAuth) {
        const filters = {
            page: this.currentPage,
            size: this.limit,
        };

        if (this.categoryId) filters.category = this.categoryId;
        if (this.search.trim() !== "") filters.search = this.search;
        if (this.sortBy.trim() !== "") filters.sortBy = this.sortBy;
        if (!(this.priceRange[0] === 0 && this.priceRange[1] === 200)) {
            filters.priceRange = [this.priceRange[0], this.priceRange[1]];
        }

        const res = isAuth ?
            await fetchProductsWithUserLikeApi(filters) :
            await fetchProductsApi(filters);

        runInAction(() => {
            this.setProducts(res.data.content);
            this.setTotalPages(res.data.totalPages);
        });
    }

    setFiltersToDefault() {
        this._search = "";
        this._sortBy = "";
        this._priceRange = [0, 200];
        this._categoryId = "";
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

    get search() {
        return this._search;
    }

    setSearch(value) {
        this._search = value;
    }

    get sortBy() {
        return this._sortBy;
    }

    setSortBy(value) {
        this._sortBy = value;
    }

    get priceRange() {
        return this._priceRange;
    }

    setPriceRange(value) {
        this._priceRange[0] = value[0];
        this._priceRange[1] = value[1];
    }

    get categoryId() {
        return this._categoryId;
    }

    setCategoryId(value) {
        this._categoryId = value;
    }
}
import ProductCard from "@/components/ProductCard"
import {useContext, useEffect, useState} from "react";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {fetchCategories, fetchProducts} from "@/http/productAPI.js";
import Pages from "@/components/Pages.jsx";
import SearchBar from "@/components/SearchBar.jsx";
import {useLocation} from "react-router-dom";

function Store() {
    const {product} = useContext(Context);
    const location = useLocation();
    const categoryFromNav = location.state?.categoryId;

    useEffect(() => {
        product.setLimit(6)

        const filters = {
            page: product.currentPage,
            size: product.limit,
        };

        if (categoryFromNav) {
            filters.category = categoryFromNav;
        }

        fetchProducts(filters).then((res) => {
            product.setProducts(res.data.content);
            product.setTotalPages(res.data.totalPages);
        })
    }, [product.currentPage, categoryFromNav]);

    useEffect(() => {
        product.setCurrentPage(0);
    }, []);

    return (
        <div className="container py-8 max-w-7xl mx-auto px-4 pt-24">
            <SearchBar className="mr-4"/>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                {product.products
                    .map((product) => (
                        <ProductCard key={product.id} product={product}/>
                    ))}
            </div>

            <Pages/>
        </div>
    );
}

export default observer(Store);
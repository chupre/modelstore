import {ProductCard} from "@/components/ProductCard"
import {useContext, useEffect, useState} from "react";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {fetchCategories, fetchProducts} from "@/http/productAPI.js";
import Pages from "@/components/Pages.jsx";
import SearchBar from "@/components/SearchBar.jsx";

function Store() {
    const {product} = useContext(Context);

    useEffect(() => {
        fetchProducts(product.currentPage, product.limit).then((res) => {
            product.setProducts(res.data.content);
            product.setTotalPages(res.data.totalPages);
        })
        fetchCategories().then((res) => {
            product.setCategories(res.data.content);
        })
    }, [product.currentPage]);

    return (
        <div className="container py-8 max-w-7xl mx-auto px-4 pt-24">
            <SearchBar/>

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
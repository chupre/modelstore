import ProductCard from "@/components/ProductCard"
import {useContext, useEffect} from "react";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import Pages from "@/components/Pages.jsx";
import SearchBar from "@/components/SearchBar.jsx";
import {useLocation} from "react-router-dom";

function Store() {
    const {product} = useContext(Context);
    const location = useLocation();
    const categoryFromNav = location.state?.categoryId;
    const backFromProductPage = location.state?.backFromProductPage;

    useEffect(() => {
        product.setLimit(6)

        if (categoryFromNav) {
            product.setCategoryId(categoryFromNav);
        }

        product.fetchProducts()
    }, [product.currentPage, categoryFromNav]);

    useEffect(() => {
        if (!backFromProductPage) {
            product.setCurrentPage(0);
        }
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
import ProductCard from "@/components/ProductCard"
import {useContext, useEffect, useState} from "react";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import Pages from "@/components/Pages.jsx";
import SearchBar from "@/components/SearchBar.jsx";
import {useLocation} from "react-router-dom";
import {fetchLikedProducts} from "@/http/productAPI.js";
import Loading from "@/components/Loading.jsx";

function Store() {
    const {product, user} = useContext(Context);
    const location = useLocation();
    const categoryFromNav = location.state?.categoryId;
    const backFromProductPage = location.state?.backFromProductPage;

    const [likes, setLikes] = useState([]);
    const [isLoading, setLoading] = useState(true);

    useEffect(() => {
        product.setLimit(9)
        product.setCategoryId(categoryFromNav);
        product.fetchProducts()

        if (user.isAuth) {
            fetchLikedProducts(user.user.sub).then((res) => {
                setLikes(res.data.productId)
                setLoading(false)
            })
        }
    }, [product.currentPage, categoryFromNav]);

    useEffect(() => {
        if (!backFromProductPage) {
            product.setCurrentPage(0);
        }
    }, []);

    function isLiked(id) {
        return likes.includes(id)
    }

    if (isLoading) {
        return <Loading/>
    }

    return (
        <div className="container py-8 max-w-7xl mx-auto px-4 pt-24">
            <SearchBar className="mr-4"/>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                {product.products
                    .map((product) => (
                        <ProductCard key={product.id} product={product} isLikedInitial={isLiked(product.id)}/>
                    ))}
            </div>

            <Pages/>
        </div>
    );
}

export default observer(Store);
import { Button } from "@/components/ui/button"
import ProductCard from "@/components/ProductCard"
import {useNavigate, useSearchParams} from "react-router-dom"
import { STORE_ROUTE } from "../utils/consts"
import {useContext, useEffect} from "react";
import {Context} from "@/main.jsx";
import {fetchProducts, fetchProductsWithUserLike} from "@/http/productAPI.js";
import {observer} from "mobx-react-lite";
import {verify} from "@/http/userAPI.js";
import {toast} from "sonner";
import errorToast from "@/utils/errorToast.jsx";

function Home() {
  const navigate = useNavigate()
  const searchParams = useSearchParams()
  const {product, user} = useContext(Context)

  useEffect(() => {
    const filters = {
      page: 0,
      size: 3,
    };

    if (!user.isAuth) {
      fetchProducts(filters).then((res) => {
        product.setProducts(res.data.content);
      })
    } else {
      fetchProductsWithUserLike(filters).then((res) => {
        product.setProducts(res.data.content);
      })
    }

    const verificationToken = searchParams[0].get("verification")
    if (verificationToken) {
      try {
        verify(verificationToken).then(() => {
          toast("Success", {
            description: "Email verified"
          })
        })
      } catch(e) {
        errorToast(e)
      }
    }
  }, [])

  return (
    <div className="max-w-7xl mx-auto px-4 pt-20">
      <section className="drop-shadow-3xl pt-10 pb-10 text-center px-4">
        <h1 className="text-4xl sm:text-5xl font-bold mb-4">Discover 3D Assets</h1>
        <p className="text-lg text-muted-foreground mb-6">
          Browse high-quality sci-fi and fantasy models for your games, renders, or 3D printing.
        </p>
        <Button className="text-lg px-6 py-3" size="lg" onClick={() => navigate(STORE_ROUTE)}>
          Explore Store
        </Button>
      </section>

      <section className="container">
        <h2 className="text-2xl font-bold mb-6">Featured Models</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {product.products.map((product) => (
            <ProductCard key={product.id} product={product} showLike={false}/>
          ))}
        </div>
      </section>
    </div>
  )
}

export default observer(Home);

import {observer} from "mobx-react-lite"
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.jsx";
import BecomeSeller from "@/components/BecomeSeller.jsx";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Button} from "@/components/ui/button"
import {Badge} from "@/components/ui/badge"
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import {
    Dialog,
    DialogContent,
    DialogDescription, DialogFooter,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Textarea} from "@/components/ui/textarea"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {
    Plus,
    DollarSign,
    Package,
    TrendingUp,
    CreditCard,
    Heart,
    ChevronUp,
    ChevronDown,
    ChevronsUpDown
} from "lucide-react"
import {fetchCurrentSeller, fetchSellerPayouts, fetchSellerProducts} from "@/http/sellerAPI.js";
import errorToast from "@/utils/errorToast.jsx";
import Loading from "@/components/Loading.jsx";
import ProductCard from "@/components/ProductCard.jsx";
import Pages from "@/components/Pages.jsx";
import {toast} from "sonner";
import {downloadProductModel} from "@/http/productAPI.js";
import {createProduct, patchProduct, deleteProduct} from "@/http/sellerAPI.js";
import {useNavigate} from "react-router-dom";
import {PRODUCT_ROUTE} from "@/utils/consts.js";
import {getStatusColor} from "@/components/OrderCard.jsx";
import {formatDate} from "@/components/CommentCard.jsx";

function SellerDashboard() {
    const {user, product} = useContext(Context)

    const [isLoading, setIsLoading] = useState(true)
    const [seller, setSeller] = useState(null)

    const [productsCurrentPage, setProductsCurrentPage] = useState(0)
    const [productsTotalPages, setProductsTotalPages] = useState(0)
    const [products, setProducts] = useState(null)

    const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
    const [editingProduct, setEditingProduct] = useState(null)
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        price: '',
        previewImage: null,
        file: null,
        categoryId: '',
    })
    const [imagePreview, setImagePreview] = useState(null)

    const [sortColumn, setSortColumn] = useState("title")
    const [sortDirection, setSortDirection] = useState("asc")

    const [activeTab, setActiveTab] = useState("products")

    const navigate = useNavigate()

    const [payouts, setPayouts] = useState(null)
    const [payoutsCurrentPage, setPayoutsCurrentPage] = useState(0)
    const [payoutsTotalPages, setPayoutsTotalPages] = useState(0)
    const [payoutsLoading, setPayoutsLoading] = useState(true)

    useEffect(() => {
        setIsLoading(true);
        fetchCurrentSeller()
            .then((res) => {
                setSeller(res.data);
            })
            .catch(errorToast);
    }, []);

    useEffect(() => {
        if (!seller?.id) return;

        fetchProducts()
    }, [seller, productsCurrentPage]);

    useEffect(() => {
        setPayoutsLoading(true)
        fetchSellerPayouts().then((res) => {
            setPayouts(res.data.content)
            setPayoutsTotalPages(res.data.totalPages)
        }).catch(errorToast)
          .finally(() => setPayoutsLoading(false))
    }, [payoutsCurrentPage]);

    if (isLoading || payoutsLoading) {
        return <Loading/>
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (formData.categoryId === '') {
            toast.error("Please select a category")
            return
        }

        const productFormData = new FormData()
        productFormData.append("title", formData.title)
        productFormData.append("description", formData.description)
        productFormData.append("price", formData.price)
        if (editingProduct == null || (editingProduct && formData.previewImage != null))
            productFormData.append("previewImage", formData.previewImage)
        if (editingProduct == null || (editingProduct && formData.file != null))
            productFormData.append("file", formData.file)
        productFormData.append("categoryId", formData.categoryId)

        try {
            editingProduct ? await patchProduct(editingProduct.id, productFormData) : await createProduct(productFormData);
            setIsCreateDialogOpen(false)
            resetForm()

            fetchProducts()

            toast.success("Success", {
                description: "Product successfully created",
            })
        } catch (e) {
            errorToast(e)
        }
    }

    const resetForm = () => {
        setEditingProduct(null)
        setImagePreview(null)
        setFormData({
            title: '',
            description: '',
            price: '',
            previewImage: null,
            file: null,
            categoryId: '',
        })
    }

    function handleDownloadModel() {
        if (editingProduct.file) {
            try {
                downloadProductModel(editingProduct.id).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const a = document.createElement("a");
                    a.href = url;
                    a.download = editingProduct.title + ".stl";
                    document.body.appendChild(a);
                    a.click();
                    a.remove();
                    window.URL.revokeObjectURL(url);
                })
            } catch (e) {
                errorToast(e)
            }
        }
    }

    const handleEdit = (product) => {
        setEditingProduct(product)
        setFormData({
            title: product.title,
            description: product.description,
            price: product.price,
            previewImage: null,
            file: null,
            categoryId: String(product.category.id),
        })
        setImagePreview(import.meta.env.VITE_API_URL + product.previewImage)
        setIsCreateDialogOpen(true)
    }

    const handleDelete = (product) => {
        try {
            deleteProduct(product.id).then(() => {
                fetchProducts()
                toast.success("Success", {
                    description: "Product successfully created",
                })
            })
        } catch (e) {
            errorToast(e)
        }
    }

    function fetchProducts(sortColumn, sortDirection) {
        setIsLoading(true);
        const sort = sortColumn && sortDirection ? `${sortColumn},${sortDirection}` : 'id'
        fetchSellerProducts(user.user.sub, productsCurrentPage, 4, sort)
            .then((res) => {
                setProducts(res.data.content);
                setProductsTotalPages(res.data.totalPages);
                setIsLoading(false);
            })
            .catch(errorToast);
    }


    const handleSort = (column) => {
        let newDirection = "asc"
        if (sortColumn === column) {
            newDirection = sortDirection === "asc" ? "desc" : "asc"
        }
        setSortColumn(column)
        setSortDirection(newDirection)

        fetchProducts(column, newDirection)
    }

    const renderSortArrow = (column) => {
        if (sortColumn !== column) {
            return <ChevronsUpDown className="w-4 h-4 inline-block ml-1 text-muted-foreground"/>
        }
        return sortDirection === "asc" ? (
            <ChevronUp className="w-4 h-4 inline-block ml-1"/>
        ) : (
            <ChevronDown className="w-4 h-4 inline-block ml-1"/>
        )
    }

    return (
        <div className="container mx-auto p-6 space-y-6 mt-20 text-left bg-background/20 mb-20 rounded-2xl backdrop-blur-lg">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold">Seller Dashboard</h1>
                    <p className="text-muted-foreground">Manage your 3D models and track your performance</p>
                </div>
                <Button onClick={() => setIsCreateDialogOpen(true)}>
                    <Plus className="w-4 h-4 mr-2"/>
                    Add Product
                </Button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Revenue</CardTitle>
                        <DollarSign className="h-4 w-4 text-muted-foreground"/>
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">${seller.totalRevenue.toFixed(2)}</div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Sales</CardTitle>
                        <TrendingUp className="h-4 w-4 text-muted-foreground"/>
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">{seller.totalSales}</div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Products</CardTitle>
                        <Package className="h-4 w-4 text-muted-foreground"/>
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">{seller.totalProducts}</div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Likes</CardTitle>
                        <Heart className="h-4 w-4 text-muted-foreground"/>
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">{seller.totalLikes}</div>
                    </CardContent>
                </Card>
            </div>

            <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-4">
                <TabsList className="grid w-full grid-cols-3 bg-card">
                    <TabsTrigger value="products">Products</TabsTrigger>
                    <TabsTrigger value="analytics">Analytics</TabsTrigger>
                    <TabsTrigger value="payouts">Payouts</TabsTrigger>
                </TabsList>

                <TabsContent value="products" className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                        {products.map((product) => (
                            <ProductCard
                                key={product.id}
                                product={product}
                                showLike={false}
                                onDelete={(product) => handleDelete(product)}
                                onEdit={(product) => handleEdit(product)}
                            />
                        ))}
                    </div>
                    <Pages totalPages={productsTotalPages} setCurrentPage={setProductsCurrentPage}
                           currentPage={productsCurrentPage}/>
                </TabsContent>

                <TabsContent value="analytics" className="space-y-4">
                    <Card>
                        <CardHeader>
                            <CardTitle>Product Performance</CardTitle>
                            <CardDescription>Detailed stats for each of your models</CardDescription>
                        </CardHeader>
                        <CardContent>
                            <div className="overflow-x-auto">
                                <table className="w-full">
                                    <thead>
                                    <tr className="border-b">
                                        <th
                                            className="text-left p-2 cursor-pointer hover:bg-muted/50 transition-colors select-none"
                                            onClick={() => handleSort("title")}
                                        >
                                            Product{renderSortArrow("title")}
                                        </th>
                                        <th
                                            className="text-left p-2 cursor-pointer hover:bg-muted/50 transition-colors select-none"
                                            onClick={() => handleSort("price")}
                                        >
                                            Price{renderSortArrow("price")}
                                        </th>
                                        <th
                                            className="text-left p-2 cursor-pointer hover:bg-muted/50 transition-colors select-none"
                                            onClick={() => handleSort("sales")}
                                        >
                                            Sales{renderSortArrow("sales")}
                                        </th>
                                        <th
                                            className="text-left p-2 cursor-pointer hover:bg-muted/50 transition-colors select-none"
                                            onClick={() => handleSort("revenue")}
                                        >
                                            Revenue{renderSortArrow("revenue")}
                                        </th>
                                        <th
                                            className="text-left p-2 cursor-pointer hover:bg-muted/50 transition-colors select-none"
                                            onClick={() => handleSort("categoryName")}
                                        >
                                            Category{renderSortArrow("categoryName")}
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {products
                                        .map((product) => (
                                            <tr key={product.id} className="border-b">
                                                <td
                                                    className="p-2 font-medium cursor-pointer hover:text-primary transition-colors"
                                                    onClick={() => navigate(`${PRODUCT_ROUTE}/${product.id}`)}
                                                >
                                                    {product.title}
                                                </td>
                                                <td className="p-2">${product.price}</td>
                                                <td className="p-2">{product.sales}</td>
                                                <td className="p-2">${product.revenue}</td>
                                                <td className="p-2">{product.category.name}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                                <Pages totalPages={productsTotalPages} setCurrentPage={setProductsCurrentPage}
                                       currentPage={productsCurrentPage}/>
                            </div>
                        </CardContent>
                    </Card>
                </TabsContent>

                <TabsContent value="payouts" className="space-y-4">
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
                        <Card>
                            <CardHeader>
                                <CardTitle>Payout Settings</CardTitle>
                                <CardDescription>Manage your payout preferences</CardDescription>
                            </CardHeader>
                            <CardContent>
                                <BecomeSeller
                                    mode="update"
                                    initialMethod={seller.payoutMethod}
                                    initialDestination={seller.payoutDestination}
                                    onSuccess={() => fetchCurrentSeller().then(res => setSeller(res.data))}
                                />
                            </CardContent>
                        </Card><Card className="flex flex-col">
                        <CardHeader>
                            <CardTitle>Payout History</CardTitle>
                            <CardDescription>Your recent payouts</CardDescription>
                        </CardHeader>
                        <CardContent className="flex-1">
                            <div className="space-y-3">
                                {payouts.map((payout) => (
                                    <div
                                        key={payout.id}
                                        className="flex items-center justify-between p-3 border rounded-lg"
                                    >
                                        <div className="flex items-center space-x-3">
                                            <CreditCard className="w-5 h-5 text-muted-foreground" />
                                            <div>
                                                <p className="font-medium">${payout.amount}</p>
                                                <p className="text-sm text-muted-foreground">
                                                    {formatDate(payout.createdAt)}
                                                </p>
                                            </div>
                                        </div>
                                        <Badge variant={getStatusColor(payout.status)}>{payout.status}</Badge>
                                    </div>
                                ))}
                            </div>
                        </CardContent>
                        <div className="mt-auto p-4">
                            <Pages
                                totalPages={payoutsTotalPages}
                                currentPage={payoutsCurrentPage}
                                setCurrentPage={setPayoutsCurrentPage}
                            />
                        </div>
                    </Card>
                    </div>
                </TabsContent>
            </Tabs>

            <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
                <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
                    <DialogHeader>
                        <DialogTitle>{editingProduct ? 'Edit Product' : 'Add New Product'}</DialogTitle>
                        <DialogDescription>
                            {editingProduct ? 'Update product information' : 'Create a new 3D model product'}
                        </DialogDescription>
                    </DialogHeader>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="grid grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <Label htmlFor="title">Product Title</Label>
                                <Input
                                    id="title"
                                    value={formData.title}
                                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                                    placeholder="Enter product name"
                                    required
                                />
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="price">Price ($)</Label>
                                <Input
                                    id="price"
                                    type="number"
                                    step="0.01"
                                    min="0"
                                    value={formData.price}
                                    onChange={(e) => setFormData({...formData, price: e.target.value})}
                                    placeholder="0.00"
                                    className="[&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none [-moz-appearance:textfield]"
                                    required
                                />
                            </div>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="description">Description</Label>
                            <Textarea
                                id="description"
                                value={formData.description}
                                onChange={(e) => setFormData({...formData, description: e.target.value})}
                                placeholder="Describe your 3D model..."
                                rows={3}
                            />
                        </div>

                        <div className="grid grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <Label htmlFor="category">Category</Label>
                                <Select value={formData.categoryId} onValueChange={(value) => setFormData({
                                    ...formData,
                                    categoryId: value
                                })}>
                                    <SelectTrigger className="w-48">
                                        <SelectValue placeholder="Select a category"/>
                                    </SelectTrigger>
                                    <SelectContent className="max-h-[200px] overflow-y-auto">
                                        {product.categories.map((category) => (
                                            <SelectItem key={category.id} value={String(category.id)}>
                                                {category.name}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            </div>
                        </div>

                        <div className="space-y-4">
                            <div className="space-y-2">
                                <Label htmlFor="imageFile">Preview Image</Label>
                                <div className="space-y-3">
                                    <Input
                                        id="imageFile"
                                        type="file"
                                        accept="image/*"
                                        onChange={(e) => {
                                            const file = e.target.files?.[0]
                                            if (file) {
                                                const previewUrl = URL.createObjectURL(file)
                                                setImagePreview(previewUrl)
                                                setFormData({...formData, previewImage: file})
                                            }
                                        }}
                                        className="mt-1 file:pt-0.5 file:mr-4"
                                        required={!editingProduct?.previewImage}
                                    />
                                    {imagePreview && (
                                        <div className="">
                                            <img
                                                src={imagePreview}
                                                alt="Product preview"
                                                className="w-32 h-32 object-cover rounded-md border flex-shrink-0"
                                            />
                                            <p className="text-xs text-muted-foreground pt-3">{imagePreview.replace(import.meta.env.VITE_API_URL + '/images/', "")}</p>
                                        </div>
                                    )}
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="modelFile">3D Model File</Label>
                                <div className="space-y-3">
                                    <Input
                                        id="modelFile"
                                        type="file"
                                        accept=".stl"
                                        onChange={(e) => {
                                            const file = e.target.files?.[0]
                                            if (file) {
                                                setFormData({...formData, file: file})
                                            }
                                        }}
                                        className="mt-1 file:pt-0.5 file:mr-4"
                                        required={!editingProduct?.file}
                                    />
                                    {editingProduct?.file && (
                                        <div className="flex items-center gap-2 p-3 rounded-md">
                                            <div className="flex-1">
                                                <p className="text-sm font-medium">Current Model</p>
                                                <p className="text-xs text-muted-foreground">{editingProduct.file}</p>
                                            </div>
                                            <Button
                                                type="button"
                                                variant="outline"
                                                size="sm"
                                                onClick={() => handleDownloadModel()}
                                            >
                                                Download
                                            </Button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        <DialogFooter className="gap-2">
                            <Button type="button" variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                                Cancel
                            </Button>
                            <Button type="submit" className="min-w-[100px]">
                                {editingProduct ? 'Update' : 'Create'} Product
                            </Button>
                        </DialogFooter>
                    </form>
                </DialogContent>
            </Dialog>
        </div>
    )
}

export default observer(SellerDashboard)
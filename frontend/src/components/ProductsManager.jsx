import {useContext, useEffect, useState} from "react"
import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Textarea} from "@/components/ui/textarea"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Plus, Edit, Trash2, AlertCircleIcon, AlertCircle} from 'lucide-react'
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {createProduct, deleteProduct, fetchCategories, fetchProduct, fetchProducts} from "@/http/productAPI.js";
import SearchBar from "@/components/SearchBar.jsx";
import Pages from "@/components/Pages.jsx";
import {toast} from "sonner";
import Loading from "@/components/Loading.jsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle, AlertDialogTrigger
} from "@/components/ui/alert-dialog.js";

function ProductsManager() {
    const {product} = useContext(Context);
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [editingProduct, setEditingProduct] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [imagePreview, setImagePreview] = useState(null)

    const [formData, setFormData] = useState({
        title: '',
        description: '',
        price: '',
        previewImage: null,
        file: null,
        ownerId: '',
        categoryId: '',
    })


    useEffect(() => {
        product.setLimit(8)

        product.fetchProducts().then((res) => {
            setIsLoading(false)
        })
    }, [product.currentPage])

    useEffect(() => {
        product.setCurrentPage(0);
        product.setFiltersToDefault();
    }, []);

    if (isLoading) {
        return <Loading></Loading>
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
        productFormData.append("previewImage", formData.previewImage)
        productFormData.append("file", formData.file)
        productFormData.append("ownerId", formData.ownerId)
        productFormData.append("categoryId", formData.categoryId)

        try {
            const res = await createProduct(productFormData)
            setIsDialogOpen(false)
            resetForm()
            setIsLoading(true)
            product.fetchProducts().then(() => {
                setIsLoading(false)
            })
            toast.success("Success", {
                description: "Product successfully created",
            })
        } catch (e) {
            const responseData = e?.response?.data;
            if (responseData && typeof responseData === "object") {
                const messages = Object.values(responseData);

                toast.error('Error', {
                    description: (
                        messages.map((msg, i) => (
                            <p key={i}>{msg}</p>
                        ))
                    ),
                });
            } else {
                toast.error("Unexpected error", {
                    description: "Something went wrong. Please try again.",
                });
            }
        }
    }

    const handleEdit = (product) => {
        setEditingProduct(product)
        setIsDialogOpen(true)
    }

    const handleDelete = async (id) => {
        try {
            deleteProduct(id).then(() => {
                setIsLoading(true)
                product.fetchProducts().then(() => {
                    setIsLoading(false)
                })
                toast.success("Success", {
                    description: "Product successfully created",
                })
            })
        } catch (e) {
            const responseData = e?.response?.data;
            if (responseData && typeof responseData === "object") {
                const messages = Object.values(responseData);

                toast.error('Error', {
                    description: (
                        messages.map((msg, i) => (
                            <p key={i}>{msg}</p>
                        ))
                    ),
                });
            } else {
                toast.error("Unexpected error", {
                    description: "Something went wrong. Please try again.",
                });
            }
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
            ownerId: '',
            categoryId: '',
        })
    }

    function handleDownloadModel() {

    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <div>
                        <CardTitle>Products Management</CardTitle>
                        <CardDescription>View, create, edit and delete products</CardDescription>
                    </div>
                    <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                        <DialogTrigger asChild>
                            <Button onClick={resetForm}>
                                <Plus className="h-4 w-4"/>
                                Add Product
                            </Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
                            <DialogHeader>
                                <DialogTitle>
                                    {editingProduct ? 'Edit Product' : 'Add New Product'}
                                </DialogTitle>
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
                                            value={formData.name}
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
                                        required
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
                                    <div className="space-y-2">
                                        <Label htmlFor="ownerId">Owner ID</Label>
                                        <Input
                                            id="ownerId"
                                            value={formData.ownerId}
                                            onChange={(e) => setFormData({...formData, ownerId: e.target.value})}
                                            placeholder="Enter owner ID"
                                            required
                                        />
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
                                                required
                                            />
                                            {imagePreview && (
                                                <div className="flex items-start gap-3">
                                                    <img
                                                        src={imagePreview || "/placeholder.svg"}
                                                        alt="Product preview"
                                                        className="w-32 h-32 object-cover rounded-md border flex-shrink-0"
                                                    />
                                                    <Button
                                                        type="button"
                                                        variant="outline"
                                                        size="sm"
                                                        onClick={() => {
                                                            setImagePreview(null)
                                                            setFormData({...formData, previewImage: null})
                                                        }}
                                                    >
                                                        Remove
                                                    </Button>
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
                                                required
                                            />
                                            {editingProduct?.modelUrl && (
                                                <div className="flex items-center gap-2 p-3 bg-muted rounded-md">
                                                    <div className="flex-1">
                                                        <p className="text-sm font-medium">Current Model</p>
                                                        <p className="text-xs text-muted-foreground">{editingProduct.name}_model</p>
                                                    </div>
                                                    <Button
                                                        type="button"
                                                        variant="outline"
                                                        size="sm"
                                                        onClick={handleDownloadModel}
                                                    >
                                                        Download
                                                    </Button>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                <DialogFooter className="gap-2">
                                    <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
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
            </CardHeader>
            <CardContent>
                <SearchBar/>
                <Table className="table-fixed w-full">
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-10">ID</TableHead>
                            <TableHead className="w-1/5">Name</TableHead>
                            <TableHead className="w-1/5">Category</TableHead>
                            <TableHead className="w-20">Price</TableHead>
                            <TableHead className="w-20">Owner ID</TableHead>
                            <TableHead className="w-28">Created</TableHead>
                            <TableHead className="text-right pr-10 w-28">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {product.products.map((product) => (
                            <TableRow key={product.id} className="text-gray-300">
                                <TableCell className="truncate">{product.id}</TableCell>
                                <TableCell className="truncate">{product.title}</TableCell>
                                <TableCell className="truncate">{product.category.name}</TableCell>
                                <TableCell>${product.price.toFixed(2)}</TableCell>
                                <TableCell className="truncate">{product.owner.username}</TableCell>
                                <TableCell>{new Date(product.createdAt).toLocaleDateString()}</TableCell>
                                <TableCell>
                                    <div className="flex items-center gap-2 justify-end">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleEdit(product)}
                                        >
                                            <Edit className="h-4 w-4"/>
                                        </Button>

                                        <AlertDialog>
                                            <AlertDialogTrigger
                                                className="h-8 rounded-md gap-1.5 px-3 border bg-background shadow-xs hover:bg-accent hover:text-accent-foreground dark:bg-input/30 dark:border-input dark:hover:bg-input/50"
                                            >
                                                <Trash2 className="h-4 w-4"/>
                                            </AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        This action cannot be undone. This will permanently delete this
                                                        product
                                                        and remove your data from our servers.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction
                                                        onClick={() => handleDelete(product.id)}>Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <Pages/>
            </CardContent>
        </Card>
    )
}

export default observer(ProductsManager)

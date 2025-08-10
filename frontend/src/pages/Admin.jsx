import { useState } from "react"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Package, ShoppingCart, Users, FolderOpen } from 'lucide-react'
import CategoriesManager from "@/components/CategoryManager.jsx"
import CartsManager from "@/components/CartManager.jsx"
// import UsersManager from "@/components/admin/users-manager"
// import FileUploadManager from "@/components/admin/file-upload-manager"
import ProductsManager from "@/components/ProductsManager.jsx";

function AdminPage() {
    const [activeTab, setActiveTab] = useState("products")

    return (
        <div className="min-h-screen bg-background text-left">
            <div className="border-b">
                <div className="flex h-16 items-center px-4">
                    <h1 className="text-2xl font-bold">3D Model Store Admin</h1>
                </div>
            </div>

            <div className="flex-1 space-y-4 p-8 pt-6">
                <div className="flex items-center justify-between space-y-2">
                    <h2 className="text-3xl font-bold tracking-tight">Dashboard</h2>
                </div>

                <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-4">
                    <TabsList className="grid w-full grid-cols-4">
                        <TabsTrigger value="products" className="flex items-center gap-2">
                            <Package className="h-4 w-4" />
                            Products
                        </TabsTrigger>
                        <TabsTrigger value="categories" className="flex items-center gap-2">
                            <FolderOpen className="h-4 w-4" />
                            Categories
                        </TabsTrigger>
                        <TabsTrigger value="carts" className="flex items-center gap-2">
                            <ShoppingCart className="h-4 w-4" />
                            Carts
                        </TabsTrigger>
                        <TabsTrigger value="users" className="flex items-center gap-2">
                            <Users className="h-4 w-4" />
                            Users
                        </TabsTrigger>
                    </TabsList>

                    <TabsContent value="products" className="space-y-4">
                        <ProductsManager />
                    </TabsContent>

                    <TabsContent value="categories" className="space-y-4">
                        <CategoriesManager />
                    </TabsContent>

                    <TabsContent value="carts" className="space-y-4">
                        <CartsManager />
                    </TabsContent>

                    {/*<TabsContent value="users" className="space-y-4">*/}
                    {/*    <UsersManager />*/}
                    {/*</TabsContent>*/}

                    {/*<TabsContent value="uploads" className="space-y-4">*/}
                    {/*    <FileUploadManager />*/}
                    {/*</TabsContent>*/}
                </Tabs>
            </div>
        </div>
    )
}

export default AdminPage;

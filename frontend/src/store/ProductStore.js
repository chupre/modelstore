import { makeAutoObservable } from 'mobx'

export default class ProductStore {
    constructor() {
        this._categories = [
            { id: 1, name: 'Sci-Fi' },
            { id: 2, name: 'Fantasy' }
        ]
        this._products = [
            {
                id: 1,
                title: "Galactic Cruiser",
                description: "A detailed 3D model of a futuristic starship.",
                price: 49.99,
                previewImage: "/images/galactic-cruiser.jpg",
                file: "galactic-cruiser.obj",
                ownerId: 1,
                categoryId: 1 // Sci-Fi
            },
            {
                id: 2,
                title: "Alien Mech",
                description: "Combat-ready alien mech with textured armor.",
                price: 59.99,
                previewImage: "/images/alien-mech.jpg",
                file: "alien-mech.obj",
                ownerId: 2,
                categoryId: 1 // Sci-Fi
            },
            {
                id: 3,
                title: "Elven Sword",
                description: "Elegant sword model with intricate engravings.",
                price: 34.50,
                previewImage: "/images/elven-sword.jpg",
                file: "elven-sword.obj",
                ownerId: 1,
                categoryId: 2 // Fantasy
            },
            {
                id: 4,
                title: "Dragon Statue",
                description: "High-poly model of a fantasy dragon statue.",
                price: 79.00,
                previewImage: "/images/dragon-statue.jpg",
                file: "dragon-statue.obj",
                ownerId: 2,
                categoryId: 2 // Fantasy
            }
        ]

        makeAutoObservable(this);
    }

    setCaregories(categories) {
        this._categories = categories;
    }
    setProducts(products) {
        this._products = products;
    }

    get categories() {
        return this._categories;
    }
    get products() {
        return this._categories;
    }
}
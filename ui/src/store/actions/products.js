import * as actionTypes from './actionTypes';

export const loadProducts = () => {
    return {
        type: actionTypes.LOAD_PRODUCTS,
        products: [
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 2, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 3, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 4, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 5, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 6, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },

        ]
    }
}

export const loadGenders = () => {
    return {
        type: actionTypes.LOAD_GENDER,
        gender: [
            {key: 'm', text: 'Male', value: 'male'},
            {key: 'f', text: 'Female', value: 'female'}
        ]
    }
}

export const loadTypes = () => {
    return {
        type: actionTypes.LOAD_TYPE,
        types: [
            {key: '1', text: 'Hoodie', value: 'hoodie'},
            {key: '2', text: 'T-Shirt', value: 'tshirt'},
            {key: '3', text: 'Sweatshirt', value: 'sweatshirt'},
            {key: '4', text: 'Jacket', value: 'jacket'},
        ]
    }
}

export const loadSize = () => {
    return {
        type: actionTypes.LOAD_SIZE,
        size: [
            {key: '1', text: 'Small', value: 'small'},
            {key: '2', text: 'Medium', value: 'medium'},
            {key: '3', text: 'Large', value: 'large'},
            {key: '4', text: 'XL', value: 'xl'},
        ]
    }
}
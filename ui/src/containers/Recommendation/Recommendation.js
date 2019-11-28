import React, { Component } from 'react';
import Carousel from '@brainhubeu/react-carousel';
import {Icon} from "semantic-ui-react";
import Product from '../Products/Product';
import './Recommendation.css';
import '@brainhubeu/react-carousel/lib/style.css';



class Recommendation extends Component {
    state = {
        products: [
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            }]
    };

    render() {

        return (
            <div>
                <Carousel
                    slidesPerPage={3}
                    arrowLeft={<Icon name="arrow left" color='yellow' size='large'/>}
                    arrowRight={<Icon name="arrow right" color='yellow' size='large'/>}
                    addArrowClickHandler
                    infinite>
                    {this.state.products.map((product, index) => (
                        <Product key={index} name={product.name}/>
                    ))}
                </Carousel>
            </div>
        );
    }
}

export default Recommendation;
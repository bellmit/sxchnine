import React, { Component } from 'react';
import { connect } from 'react-redux';
import Carousel from '@brainhubeu/react-carousel';
import {Icon} from "semantic-ui-react";
import Product from '../Products/Product';
import '@brainhubeu/react-carousel/lib/style.css';
import './Recommendation.css';



class Recommendation extends Component {

    render() {

        return (
            <div>
                <Carousel
                    slidesPerPage={3}
                    arrowLeft={<Icon name="arrow left" color='yellow' size='large'/>}
                    arrowRight={<Icon name="arrow right" color='yellow' size='large'/>}
                    addArrowClickHandler
                    infinite>
                    {this.props.products.slice(0, 3).map((product, index) => (
                        <Product key={index}
                                 name={product.name}
                                 image={product.images}
                                 logo={product.logo}
                                 brand={product.brand}
                                 price={product.price}
                                 size={product.size}
                                 id={product.id}
                                 height="120%"
                                 width="80%"
                        />
                    ))}
                </Carousel>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        products: state.products.products,
    }
}

export default connect(mapStateToProps)(Recommendation);
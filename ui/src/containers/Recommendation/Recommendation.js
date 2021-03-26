import React, { Component } from 'react';
import { connect } from 'react-redux';
import Carousel from '@brainhubeu/react-carousel';
import {Icon} from "semantic-ui-react";
import Product from '../Products/Product';
import '@brainhubeu/react-carousel/lib/style.css';
import './Recommendation.css';
import * as actions from "../../store/actions";



class Recommendation extends Component {

    selectProductHandler = (id) => {
        this.props.loadProduct(id, this.props.history);
    };

    render() {

        return (
            <div>
                <Carousel
                    slidesPerPage={3}
                    itemWidth={160}
                    arrowLeft={<Icon name="arrow left" color='yellow' size='large'/>}
                    arrowRight={<Icon name="arrow right" color='yellow' size='large'/>}
                    addArrowClickHandler
                    infinite
                    centered>
                    {this.props.products.slice(0, 5).map((product, index) => (
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
                                 clicked={() => this.selectProductHandler(product.id)}
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

const dispatchToProps = dispatch => {
    return {
        loadProduct: (id, history) => dispatch(actions.loadProduct(id, history))
    }
}

export default connect(mapStateToProps, dispatchToProps)(Recommendation);
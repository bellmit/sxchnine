import React from "react";
import { Carousel } from 'react-bootstrap';
import ProductDetails from './ProductDetails';
import './Shopping.css';
import front_500 from './images/front-500.jpg'
import back_500 from './images/back-500.jpg'
import Contact from "../Contact/Contact";
import logo from '../Head/logo_got_it.png';
import Recommendation from "../Recommendation/Recommendation";


const productSlick = () => {

    return (
        <div>
            <div className="Yellow-bar-div" />
            <div>
                <img className="Got-it-logo" src={logo}/>
            </div>
            <div className="Slick-Container-div">
            <Carousel fade={true} keyboard style={{textAlign: "center"}}>
                <Carousel.Item>
                    <div><img src={front_500} /></div>
                </Carousel.Item>
                <Carousel.Item>
                    <div><img src={back_500} /> </div>
                </Carousel.Item>
            </Carousel>
            </div>
            <div className="Product-Details-Div">
                <ProductDetails />
            </div>
            <div className="Product-Details-Empty-Div"/>

            <div className="Reco-Container-div">
                <Recommendation />
            </div>
            <div className="Product-Details-footer">
                <Contact/>
            </div>
        </div>
    );
}

export default productSlick;
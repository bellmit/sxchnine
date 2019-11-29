import React, {Component} from "react";
import { Carousel } from 'react-bootstrap';
import ProductDetails from './ProductDetails';
import Contact from "../../components/Contact/Contact";
import Recommendation from "../../containers/Recommendation/Recommendation";
import ShopResume from '../ShopResume/ShopResume';
import './ProductSlick.css';
import front_500 from './images/front-500.jpg'
import back_500 from './images/back-500.jpg'
import logo from '../../components/Head/logo_got_it.png';


class ProductSlick extends Component {

    componentDidMount(){
        console.log(this.props);
    }

    render(){

    let size = 5;
    return (
        <div>
            <div className="Yellow-bar-div" />
            <div>
                <img alt="" className="Got-it-logo" src={logo}/>
            </div>
            <div>
                <ShopResume size = {size} {...this.props}/>
            </div>
            <div className="Slick-Container-div">
            <Carousel fade={true} keyboard style={{textAlign: "center"}}>
                <Carousel.Item>
                    <div><img alt= "" src={front_500} /></div>
                </Carousel.Item>
                <Carousel.Item>
                    <div><img alt="" src={back_500} /> </div>
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
}

export default ProductSlick;
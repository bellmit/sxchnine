import React, {Component} from "react";
import {Carousel} from 'react-bootstrap';
import {connect} from 'react-redux';
import ProductDetails from './ProductDetails';
import Contact from "../Contact/Contact";
import Recommendation from "../../containers/Recommendation/Recommendation";
import ShopResume from '../ShopResume/ShopResume';
import User from '../User/User';
import './ProductSlick.css';


class ProductSlick extends Component {

    render() {
        return (
            <div>
                <div className="Yellow-bar-div"/>
                <div>
                    <ShopResume {...this.props}/>
                </div>
                <div>
                    <User {...this.props}/>
                </div>
                <div className="Slick-Container-div">
                    <Carousel fade={true} keyboard style={{textAlign: "center"}}>
                        {this.props.product.images.map((image, index) => (
                            <Carousel.Item key={index}>
                                <div><img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                                          src={image}/></div>
                            </Carousel.Item>
                        ))}
                    </Carousel>
                </div>
                <div className="Product-Details-Div">
                    <ProductDetails {...this.props} />
                </div>
                <div className="Product-Details-Empty-Div"/>

                <div className="Reco-Container-div">
                    <Recommendation/>
                </div>
                <div className="Product-Details-footer">
                    <Contact/>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        product: state.product.product
    }
}


export default connect(mapStateToProps)(ProductSlick);
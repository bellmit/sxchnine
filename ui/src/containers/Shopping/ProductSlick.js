import React, {Component} from "react";
import { Carousel } from 'react-bootstrap';
import { connect } from 'react-redux';
import ProductDetails from './ProductDetails';
import Contact from "../Contact/Contact";
import Recommendation from "../../containers/Recommendation/Recommendation";
import ShopResume from '../ShopResume/ShopResume';
import User from '../User/User';
import './ProductSlick.css';


class ProductSlick extends Component {

    componentDidMount(){
        console.log("ProductSlick did mount");
        console.log(this.props);
        console.log(this.props.product);
    }

    render(){

    return (
        <div>
            <div className="Yellow-bar-div" />
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
                        <div><img alt= "" src={image} /></div>
                    </Carousel.Item>
                ))}


{/*                <Carousel.Item>
                    <div><img alt= "" src={this.props.product.images[0]} /></div>
                </Carousel.Item>
                <Carousel.Item>
                    <div><img alt="" src={this.props.product.images[0]} /> </div>
                </Carousel.Item>*/}
            </Carousel>
            </div>
            <div className="Product-Details-Div">
                <ProductDetails {...this.props} />
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

const mapStateToProps = state => {
    return {
        product: state.product.product
    }
}


export default connect(mapStateToProps)(ProductSlick);
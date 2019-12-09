import React, {Component} from "react";
import Aux from '../../hoc/Aux/Aux';
import bannerMen from './rsz_1screen_3.png';
import bannerWomen from './women_banner.jpg';
import orderPlace from './order_place.jpg';
import checkout from './checkout.jpg';
import './Banner.css';

class Banner extends Component {


    shouldComponentUpdate(nextProps, nextState) {
        return this.props.location.pathname !== nextProps.location.pathname;
    }


    componentDidUpdate() {
        console.log('Banner did Update ');
        console.log(this.props);

    }

    render() {
        let banner = <Aux>
                        <img alt="" src={bannerMen} className="Banner-Header-img"/>
                        <div className="Banner-Container-Men">
                            <p>MEN </p>
                        </div>
                        <div className="Banner-Empty-Div"/>
                    </Aux>

        if (this.props.location.pathname === '/women'){
            banner = <Aux>
                <img alt="" src={bannerWomen} className="Banner-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> WOMEN </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        if (this.props.location.pathname === '/orders'){
            banner = <Aux>
                <img alt="" src={orderPlace} className="BannerOrder-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> ORDER PLACE </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        if (this.props.location.pathname === '/checkout'){
            banner = <Aux>
                <img alt="" src={checkout} className="BannerCheckout-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> CHECKOUT </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        return (
            <div>
                {banner}
            </div>
        );
    }
}

export default Banner;
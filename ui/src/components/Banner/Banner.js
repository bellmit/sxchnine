import React, {Component} from "react";
import Aux from '../../hoc/Aux/Aux';
import bannerMen from './men_banner.png';
import bannerWomen from './women_banner.jpg';
import orderPlace from './order_place.jpg';
import checkout from './checkout.jpg';
import open from './open1.jpg';
import './Banner.css';


class Banner extends Component {


    shouldComponentUpdate(nextProps, nextState) {
        return this.props.location.pathname !== nextProps.location.pathname;
    }

    render() {
        let banner = <Aux>
                        <img alt="naybxrz econcept store man vintage clothes hoodies shirts street wear street life carhartt adidas nike deadora northface"
                             src={bannerMen} className="Banner-Header-img"/>
                        <div className="Banner-Container-Men">
                            <p>MEN </p>
                        </div>
                        <div className="Banner-Empty-Div"/>
                    </Aux>

        if (this.props.location.pathname === '/women'){
            banner = <Aux>
                <img alt="naybxrz econcept store woman girls vintage clothes hoodies shirts adidas nike carhartt northface patagonia rains" src={bannerWomen} className="Banner-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> WOMEN </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        if (this.props.location.pathname === '/orders'){
            banner = <Aux>
                <img alt="naybxrz econcept store vintage clothes nike nocta adidas supreme northface reebok carhartt obey wutang wear bad boy clothes street culture lifestyle"
                     src={orderPlace}
                     className="BannerOrder-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> ORDER PLACE </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        if (this.props.location.pathname === '/checkout'){
            banner = <Aux>
                <img alt="naybxrz econcept store vintage clothes carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle" src={checkout} className="BannerCheckout-Header-img"/>
                <div className="Banner-Container-Men">
                    <p> CHECKOUT </p>
                </div>
                <div className="Banner-Empty-Div"/>
            </Aux>
        }

        if (this.props.location.pathname === '/userAccount'){
            banner = <Aux>
                <img alt="naybxrz econcept store vintage checkout carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle" src={open} className="BannerUserAccount-Header-img"/>
                <div className="Banner-Container-User">
                    <p> What you Got  </p>
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
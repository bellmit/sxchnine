import React, {Component} from 'react';
import {connect} from "react-redux";
import {persistor} from './../../index';
import Aux from '../../hoc/Aux/Aux';
import {Container, Header} from 'semantic-ui-react';
import * as actions from '../../store/actions/index';
import './Confirmation.css';
import thank from './thankYou.jpg';

class Confirmation extends Component {

    componentDidMount() {
        window.localStorage.removeItem("persist:root");
        window.localStorage.clear();
        persistor.purge();
        if (this.props.match.params.status === '1' || this.props.match.params.status === '2') {
            this.resetState();
        }
    }

    resetState = () => {
        this.props.resetProductToOrder();
    }

    render() {

        let message = <Aux>
            <Header as="h2">
                <span className="Confirmation-Message-Text-h1-div">
                    You GOT IT !!
                </span>
            </Header>
            <p className="Confirmation-Message-Text-p-div">
                Congrats. Your bag is on the way.
            </p>
            <p className="Confirmation-Message-Text-p2-div">
                An email recept including the details about your order has been sent to the email address
                provided. Please keep it for your records.
            </p>
        </Aux>

        if (this.props.match.params.status === '2') {
            message = <Aux>
                <Header as="h2">
                <span className="Confirmation-Message-Text-h1-div">
                    You GOT IT !!
                </span>
                </Header>
                <p className="Confirmation-Message-Text-p-div">
                    Congrats.
                </p>
                <p className="Confirmation-Message-Text-p2-div">
                    Your order will be process soon. An email will be sent including the details about you order.
                    Please keep it for you records.
                </p>
            </Aux>
        } else if (this.props.match.params.status === '0') {
            message = <Aux>
                <Header as="h2">
                <span className="Confirmation-Message-Text-h1-div">
                    Aight, not this time !!
                </span>
                </Header>
                <p className="Confirmation-Message-Text-p-div">
                    Oops something went wrong with your payment.
                </p>
                <p className="Confirmation-Message-Text-p2-div">
                    Your order cannot be processed ! It seems that the payment was refused by your bank.
                    Please retry again once fixed.
                </p>
            </Aux>
        }

        return (

            <div>
                <img alt="naybxrz econcept store vintage adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                     src={thank} className="Confirmation-Image-div"/>

                <div className="Confirmation-Message-div">
                    <Container text className="Confirmation-Container-div">
                        {message}
                        <div>
                            <div className="Confirmation-Yellow-second-bar-div">
                                <p className="Confirmation-Message-Text-p3-div">
                                    Spread Love, Culture & Style.
                                </p>
                            </div>
                        </div>
                    </Container>

                </div>
            </div>
        );
    }
}

const dispatchToProps = dispatch => {
    return {
        resetProductToOrder: () => dispatch(actions.resetProductToOrder())
    }
}


export default connect(null, dispatchToProps)(Confirmation);
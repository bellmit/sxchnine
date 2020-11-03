import React, {Component} from 'react';
import { persistor } from './../../index';
import Aux from '../../hoc/Aux/Aux';
import {Container, Header} from 'semantic-ui-react';
import './Confirmation.css';
import thank from './thankYou.jpg';

class Confirmation extends Component {

    componentDidMount() {
        console.log("did mount");
        console.log(window.localStorage.getItem("persist:root"));
        window.localStorage.removeItem("persist:root");
        persistor.purge();
    }

    render() {

        let message = <Aux>
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
                <img alt="" src={thank} className="Confirmation-Image-div"/>

                <div className="Confirmation-Message-div">
                    <Container text className="Confirmation-Container-div">
                        <Header as="h2">
                <span className="Confirmation-Message-Text-h1-div">
                    You GOT IT !!
                </span>
                        </Header>
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


export default Confirmation;
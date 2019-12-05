import React from 'react';
import Aux from '../../hoc/Aux/Aux';
import {Container, Header} from 'semantic-ui-react';
import './Confirmation.css';
import thank from './thankYou.jpg';

const confirmation = () => {

    let message = <Aux>
        <p className="Confirmation-Message-Text-p-div">
            Congrats. Your bag is on the way.
        </p>
            <p className="Confirmation-Message-Text-p2-div">
                An email recept including the details bout your order has been sent to the email address
                provided. Please keep it for your records.
            </p>
        </Aux>

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
                    <p className="Confirmation-Message-Text-p-div">
                        Congrats. Your bag is on the way.
                    </p>
                    <p className="Confirmation-Message-Text-p2-div">
                        An email recept including the details bout your order has been sent to the email address
                        provided. Please keep it for your records.
                    </p>

                    <div>
                        <div className="Confirmation-Yellow-second-bar-div">
                            <p className="Confirmation-Message-Text-p2-div">
                                Spread Love, Culture & Style.
                            </p>
                        </div>
                    </div>
                </Container>

            </div>
        </div>
    );
}

export default confirmation;
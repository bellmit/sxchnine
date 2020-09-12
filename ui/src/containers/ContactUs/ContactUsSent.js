import React from 'react';
import {Container} from "semantic-ui-react";
import './ContactUsSent.css';
import skate_back from './skate_back.jpg';
import Aux from "../../hoc/Aux/Aux";

const contactUsSent = (props) => {

    return (
        <div>
            <img alt="" src={skate_back} className="ContactUsSent-Image-div"/>

            <div className="ContactUsSent-Form-Div">
                <Container text className="ContactUsSent-Container-div">
                    <Aux>
                        <p className="ContactUsSent-Message-Text-p2-div">
                            Thank you !  We GOT your message.
                        </p>
                        <p className="ContactUsSent-Message-Text-p2-div">
                            You will hear from us soon.
                        </p>
                    </Aux>
                    <div className="ContactUsSent-Yellow-second-bar-div">
                        <p className="ContactUsSent-Message-Text-p3-div">
                            Spread Love, Culture & Style.
                        </p>
                    </div>
                </Container>

                <button className="ContactUsSent-Continue-Button" onClick={() => props.history.push('/')}>
                    <span className="ContactUsSent-Text-Button">BACK HOME -></span>
                </button>
            </div>

        </div>
    )
}

export default contactUsSent;
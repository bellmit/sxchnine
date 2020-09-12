import React from 'react';
import {Container, Icon} from "semantic-ui-react";
import customer_back from './customer_back.jpg';
import './CustomerService.css';
import Aux from "../../hoc/Aux/Aux";

const customerService = (props) => {
    return (
        <div>
            <img alt="" src={customer_back} className="Customer-Image-div"/>
            <div className="Customer-Form-Div">
                <Container text className="Customer-Container-div">
                    <Aux>
                        <p className="Customer-Message-Text-p2-div">
                            <Icon name="phone" />
                             To call our customer service: +1 438-925-8181
                        </p>
                        <p className="Customer-Message-Text-p2-div">
                            We will be glad to answer all your questions.
                        </p>
                    </Aux>
                    <div className="Customer-Yellow-second-bar-div" />
                    <Aux>
                        <p className="Customer-Message-Text-p2-div">
                            <Icon name="mail" />
                            To send an urgent email: support@gotit.com
                        </p>
                        <p className="Customer-Message-Text-p2-div">
                            We will answer you within 24h.
                        </p>
                    </Aux>
                </Container>
                <button className="Customer-Continue-Button" onClick={() => props.history.push('/')}>
                    <span className="Customer-Text-Button">BACK HOME -></span>
                </button>
            </div>
        </div>
    )
}

export default customerService;
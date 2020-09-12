import React, {PureComponent} from 'react';
import {Container, Header, Form, Dimmer, Loader, Grid} from "semantic-ui-react";
import tracking_back from './tracking_back.jpg';
import './Tracking.css';

class Tracking extends PureComponent {

    state = {
        loading: false,
        orderId: '',
        email: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    track = () => {
        console.log(this.state.orderId);
        console.log(this.state.email);
        this.setState({loading: true});
        this.props.history.push('/contactSent');
    };

    render() {
        return (
            <div>
                <img alt="" src={tracking_back} className="Tracking-Image-div"/>
                <Dimmer active={this.state.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="Tracking-Form-Div">
                    <Container text className="Tracking-Container-div">
                        <Header>
                            <span className="ContactUs-Message-Text-h1-div">Track your Order:</span>
                        </Header>

                        <div className="Tracking-Grid-div">
                            <Grid inverted>
                                <Grid.Row>
                                    <Form.Input required inverted
                                                style={{fontFamily: "American Typewriter, Times", fontSize: '60%'}}
                                                size="mini"
                                                placeholder='Order ID ...'
                                                className="Info-Text"
                                                name='orderId'
                                                value={this.state.orderId}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <Form.Input inverted required size="mini"
                                                style={{fontFamily: "American Typewriter", fontSize: '60%'}}
                                                placeholder='Email ...'
                                                className="Info-Text"
                                                name='email'
                                                value={this.state.email}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <button className="Tracking-Continue-Button" onClick={this.track}>
                                        <span className="Tracking-Text-Button">TRACK</span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </div>
                    </Container>
                </div>
            </div>
        );
    }
}

export default Tracking;
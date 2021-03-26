import React, {Component} from 'react';
import Aux from '../../hoc/Aux/Aux';
import {Button, Grid} from 'semantic-ui-react';
import {connect} from 'react-redux';
import './HomeSearch.css';
import * as actions from "../../store/actions";


class HomeSearch extends Component {

    state = {
        gender: '',
        category: '',
        size: ''
    };

    handleChangeGender = (e, {value}) => this.setState({value, gender: value});
    handleChangeCategories = (e, {value}) => this.setState({value, category: value});
    handleChangeSize = (e, {value}) => this.setState({value, size: value});

    searchAdvanced = () => {
        this.props.homeSearchProducts(this.state.gender, this.state.category, this.state.size, this.props.history);
    };

    handleClick = (gender) => {
        if (gender === 'MEN') {
            this.props.history.push('/men');
        } else if (gender === 'WOMEN') {
            this.props.history.push('/women');
        }
    };

    render() {
        return (
            <Aux>
                <Grid relaxed columns='equal' centered textAlign="center">
                    <Grid.Column>
                        <Button className="Home-Search-Continue-Button"
                                style={{background: 'yellow'}}
                                onClick={() => this.handleClick('MEN')}>
                            <span className="Home-Search-Text-Button">MEN</span>
                        </Button>
                    </Grid.Column>
                    <Grid.Column>
                        <Button className="Home-Search-Continue-Button"
                                style={{background: 'yellow'}}
                                onClick={() => this.handleClick('WOMEN')}>
                            <span className="Home-Search-Text-Button">WOMEN</span>
                        </Button>
                    </Grid.Column>
                </Grid>
            </Aux>
        );
    }
}

const mapDispatchToProps = dispatch => {
    return {
        homeSearchProducts: (gender, category, size, history) => dispatch(actions.homeSearchProducts(gender, category, size, history))
    }
}


export default connect(null, mapDispatchToProps)(HomeSearch);
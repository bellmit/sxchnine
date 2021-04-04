import React, {Component} from 'react';
import jordan from './jordan2.png';
import './Head.css';
import HomeSearch from '../../containers/HomeSearch/HomeSearch';
import Aux from '../../hoc/Aux/Aux';
import Connexion from "./../../containers/Connexion/Connexion";


class Head extends Component{
    render () {
        return (
            <Aux>
                <div className="Header-Body">
                    <div>
                        <img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob you know my stylo"
                             className="Container-img" src={jordan}/>
                    </div>
                    <div>
                        <p className="Paragraph">YOU KNOW MY _STEELO !</p>
                    </div>
                    <div className="Empty-Div"/>
                </div>
                <Connexion />
                <div className="Home-Search-Div">
                    <HomeSearch {...this.props}/>
                </div>
            </Aux>
        );
    }
}

export default Head;
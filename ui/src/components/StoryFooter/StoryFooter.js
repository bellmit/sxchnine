import React from 'react';
import './StoryFooter.css';
import Aux from '../../hoc/Aux/Aux';
import foot from './foot.png';


const storyFooter = (props) => {
    return (
        <Aux>
            <img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                 className="Container-footer-img"
                 src={foot}/>
            <p className="Paragraph-footer" onClick={() => props.history.push('/ourStory')}>OUR STORY </p>
            <div className="Empty-Div-footer"/>
        </Aux>
    );
}

export default storyFooter;
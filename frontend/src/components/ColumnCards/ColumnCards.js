import React, { PropTypes } from 'react';
import {TransitionMotion, spring, presets} from 'react-motion';
import Card from '../Card/Card';

const ColumnCards = React.createClass({
  getDefaultStyles() {
    const styles = this.props.columnCardIds.map((cardId) => {
      return {
        key: cardId,
        style: { opacity: 0.0 }
      }
    });
    return styles;
  },
  getStyles() {
    const styles = this.props.columnCardIds.map((cardId) => {
      return {
        key: cardId,
        style: {
          opacity: spring(1, {stiffness: 220, damping: 42})
        }
      }
    });
    return styles;
  },

  willEnter() {
    return { opacity: 0.0 }
  },
  willLeave() {
    return {
      opacity: spring(0, presets.gentle)
    };
  },

  render() {
    if (this.props.columnCardIds) {
      return (
        <TransitionMotion
          defaultStyles={this.getDefaultStyles()}
          styles={this.getStyles()}
          willEnter={this.willEnter}
          willLeave={this.willLeave}
        >
          {styles =>
            <div className="column-cards">
              {styles.map((card) => {
                return (
                  <Card key={card.key} id={card.key} style={card.style} />
                )
              })}
            </div>
          }
        </TransitionMotion>
      )
    } else {
      return null;
    }

  },

});

ColumnCards.propTypes = {
  columnCardIds: PropTypes.arrayOf(PropTypes.string.isRequired)
};

export default ColumnCards;
